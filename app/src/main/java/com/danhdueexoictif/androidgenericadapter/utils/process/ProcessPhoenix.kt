package com.danhdueexoictif.androidgenericadapter.utils.process

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.os.Parcelable
import android.os.Process
import com.danhdueexoictif.androidgenericadapter.utils.locale.LocaleHelper
import org.koin.android.ext.android.inject


/**
 * Process Phoenix facilitates restarting your application process. This should only be used for
 * things like fundamental state changes in your debug builds (e.g., changing from staging to
 * production).
 * <p>
 * Trigger process recreation by calling {@link #triggerRebirth} with a {@link Context} instance.
 */
class ProcessPhoenix : Activity(), ProcessHelper {

    private val localeHelper: LocaleHelper? by inject()

    /**
     * Call to restart the application process using the {@linkplain Intent#CATEGORY_DEFAULT default}
     * activity as an intent.
     * <p>
     * Behavior of the current process after invoking this method is undefined.
     */
    override fun triggerRebirth(context: Context) {
        triggerRebirth(context, getRestartIntent(context))
    }

    private fun getRestartIntent(context: Context): Intent {
        val packageName = context.packageName
        val defaultIntent = context.packageManager.getLaunchIntentForPackage(packageName)
        defaultIntent?.let {
            it.addFlags(FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK)
            return it
        }

        throw IllegalStateException(
            "Unable to determine default activity for "
                    + packageName
                    + ". Does an activity specify the DEFAULT category in its intent filter?"
        )
    }

    /**
     * Call to restart the application process using the specified intents.
     * <p>
     * Behavior of the current process after invoking this method is undefined.
     */
    override fun triggerRebirth(context: Context, vararg nextIntents: Intent) {
        val intent = Intent(context, ProcessPhoenix::class.java)
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK) // In case we are called with non-Activity context.
        val lstIntents = arrayListOf<Parcelable>()
        lstIntents.addAll(nextIntents)
        intent.putParcelableArrayListExtra(KEY_RESTART_INTENTS, lstIntents)
        context.startActivity(intent)
        if (context is Activity) {
            context.finish()
        }
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(localeHelper?.onAttach(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val lstIntents = intent.getParcelableArrayListExtra<Intent>(KEY_RESTART_INTENTS)
        lstIntents?.let {
            startActivities(lstIntents.toArray(arrayOfNulls(lstIntents.size)))
            finish()
        }
    }

    /**
     * Checks if the current process is a temporary Phoenix Process.
     * This can be used to avoid initialisation of unused resources or to prevent running code that
     * is not multi-process ready.
     *
     * @return true if the current process is a temporary Phoenix Process
     */
    override fun isPhoenixProcess(context: Context): Boolean {
        val currentPid = Process.myPid()
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningProcesses = manager.runningAppProcesses

        runningProcesses?.let {
            for (processInfo in it) {
                if (processInfo.pid == currentPid && processInfo.processName.endsWith(":phoenix")) {
                    return true
                }
            }
        }
        return false
    }

    companion object {
        const val KEY_RESTART_INTENTS = "phoenix_restart_intents"
    }
}
