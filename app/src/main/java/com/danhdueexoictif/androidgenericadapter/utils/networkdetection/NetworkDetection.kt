package com.danhdueexoictif.androidgenericadapter.utils.networkdetection

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.telephony.TelephonyManager
import androidx.annotation.RequiresPermission
import androidx.lifecycle.LiveData

@Suppress("DEPRECATION")
class NetworkDetection internal constructor(
    private val connectivityManager: ConnectivityManager,
    private val telephonyManager: TelephonyManager
) : LiveData<ConnectionState>() {

    var currentWifiNetwork: Int = 0

    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    constructor(application: Application) : this(
        application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager,
        application.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    )

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        val networkInfoSubtype = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            telephonyManager.networkType
        } else {
            connectivityManager.activeNetworkInfo?.subtype
        }
        override fun onAvailable(network: Network?) {
            // this ternary operation is not quite true, because non-metered doesn't yet mean, that it's wifi
            // nevertheless, for simplicity let's assume that's true
            if (connectivityManager.isActiveNetworkMetered) {
                postState(ConnectionDef.MOBILE_DATA, true, networkInfoSubtype)
            } else {
                network?.let { currentWifiNetwork = it.hashCode() }
                postState(ConnectionDef.WIFI_DATA, true, networkInfoSubtype)
            }
        }

        override fun onLost(network: Network?) {
            network?.apply {
                if (network.hashCode() == currentWifiNetwork) {
                    postState(ConnectionDef.WIFI_DATA, false, networkInfoSubtype)
                } else {
                    postState(ConnectionDef.MOBILE_DATA, false, networkInfoSubtype)
                }
            }
        }
    }

    override fun onActive() {
        super.onActive()
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            val networkCapabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            // NET_CAPABILITY_VALIDATED - Indicates that connectivity on this network was successfully validated.
            // NET_CAPABILITY_INTERNET - Indicates that this network should be able to reach the internet.
            networkCapabilities?.let {
                if (it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    && it.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                ) {
                    if (it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        postState(ConnectionDef.WIFI_DATA, true, telephonyManager.networkType)
                    } else if (it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        postState(ConnectionDef.MOBILE_DATA, true, telephonyManager.networkType)
                    }
                }
            } ?: run { postState(ConnectionDef.NO_CONNECTION, false, TelephonyManager.NETWORK_TYPE_UNKNOWN) }
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo
            networkInfo?.let {
                when (networkInfo.type) {
                    ConnectivityManager.TYPE_WIFI -> {
                        postState(ConnectionDef.WIFI_DATA, true, networkInfo.subtype)
                    }
                    ConnectivityManager.TYPE_MOBILE -> {
                        postState(ConnectionDef.MOBILE_DATA, true, networkInfo.subtype)
                    }
                }
            } ?: run { postState(ConnectionDef.NO_CONNECTION, false, TelephonyManager.NETWORK_TYPE_UNKNOWN) }
        }
        registerNetworkCallback()
    }

    private fun postState(@ConnectionDef connectionType: Int, connected: Boolean, subType: Int?) {
        val connectionState = ConnectionState(
            connectionType, connected, isConnectionFast(connectionType, subType)
        )
        postValue(connectionState)
    }

    private fun registerNetworkCallback() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        } else {
            val builder = NetworkRequest.Builder()
            connectivityManager.registerNetworkCallback(builder.build(), networkCallback)
        }
    }

    private fun isConnectionFast(@ConnectionDef type: Int?, subType: Int?): Boolean {
        if (type == ConnectionDef.WIFI_DATA) return true
        return when (subType) {
            TelephonyManager.NETWORK_TYPE_EVDO_0 -> true // ~ 400-1000 kbps
            TelephonyManager.NETWORK_TYPE_EVDO_A -> true // ~ 600-1400 kbps
            TelephonyManager.NETWORK_TYPE_HSDPA -> true // ~ 2-14 Mbps
            TelephonyManager.NETWORK_TYPE_HSPA -> true // ~ 700-1700 kbps
            TelephonyManager.NETWORK_TYPE_HSUPA -> true // ~ 1-23 Mbps
            TelephonyManager.NETWORK_TYPE_UMTS -> true // ~ 400-7000 kbps
            TelephonyManager.NETWORK_TYPE_EHRPD -> true // ~ 1-2 Mbps - API level 11
            TelephonyManager.NETWORK_TYPE_EVDO_B -> true // ~ 5 Mbps - API level 9
            TelephonyManager.NETWORK_TYPE_HSPAP -> true // ~ 10-20 Mbps - API level 13
            TelephonyManager.NETWORK_TYPE_LTE -> true // ~ 10+ Mbps - API level 11
            TelephonyManager.NETWORK_TYPE_1xRTT -> false // ~ 50-100 kbps
            TelephonyManager.NETWORK_TYPE_CDMA -> false // ~ 14-64 kbps
            TelephonyManager.NETWORK_TYPE_EDGE -> false // ~ 50-100 kbps
            TelephonyManager.NETWORK_TYPE_GPRS -> false // ~ 100 kbps
            TelephonyManager.NETWORK_TYPE_IDEN -> false // ~25 kbps - API level 8
            TelephonyManager.NETWORK_TYPE_UNKNOWN -> false
            else -> false
        }
    }

    override fun onInactive() {
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}
