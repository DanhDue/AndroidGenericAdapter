package com.danhdueexoictif.androidgenericadapter.utils.networkdetection

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import com.danhdueexoictif.androidgenericadapter.data.remote.response.HttpResponseCode
import com.danhdueexoictif.androidgenericadapter.utils.Constants
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

object NoInternetUtils {
    /**
     * Check if the device is connected with the Internet.
     */
    @Suppress("DEPRECATION")
    @JvmStatic
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val networkCapabilities =
                connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                //for other device how are able to connect with Ethernet
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                //for check internet over Bluetooth
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                // for check internet over VPN
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> true
                else -> false
            }
        } else {
            val nwInfo = connectivityManager.activeNetworkInfo ?: return false
            return nwInfo.isConnected
        }
    }

    /**
     * Check if the device is in airplane mode.
     */
    @JvmStatic
    fun isAirplaneModeOn(context: Context): Boolean {
        return Settings.System.getInt(
            context.contentResolver,
            Settings.Global.AIRPLANE_MODE_ON,
            0
        ) != 0
    }

    /**
     * Ping google.com to check if the internet connection is active.
     * It must be called from a background thread.
     */
    @JvmStatic
    fun hasActiveInternetConnection(): Boolean {
        try {
            val urlConnection =
                URL("https://www.google.com").openConnection() as HttpURLConnection

            urlConnection.setRequestProperty("User-Agent", "Test")
            urlConnection.setRequestProperty("Connection", "close")
            urlConnection.connectTimeout = Constants.DOUBLE_CHECK_CONNECTION_TIME_OUT
            urlConnection.connect()

            return urlConnection.responseCode == HttpResponseCode.HTTP_OK
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * Open the system settings.
     */
    @JvmStatic
    fun turnOnMobileData(context: Context) {
        try {
            context.startActivity(Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "It cannot open settings!", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Open the wifi settings.
     */
    @JvmStatic
    fun turnOnWifi(context: Context) {
        try {
            context.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "It cannot open settings!", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Open the airplane mode settings.
     */
    @JvmStatic
    fun turnOffAirplaneMode(context: Context) {
        try {
            context.startActivity(Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "It cannot open settings!", Toast.LENGTH_LONG).show()
        }
    }
}
