package com.danhdueexoictif.androidgenericadapter

import android.app.Application
import android.util.Log
import androidx.annotation.Nullable
import com.crashlytics.android.Crashlytics.*
import com.crashlytics.android.core.CrashlyticsCore
import com.danhdueexoictif.androidgenericadapter.di.appModule
import com.danhdueexoictif.androidgenericadapter.di.networkModule
import com.danhdueexoictif.androidgenericadapter.di.repositoryModule
import com.danhdueexoictif.androidgenericadapter.di.viewModelModule
import com.danhdueexoictif.androidgenericadapter.utils.Constants.CRASHLYTICS_KEY_MESSAGE
import com.danhdueexoictif.androidgenericadapter.utils.Constants.CRASHLYTICS_KEY_PRIORITY
import com.danhdueexoictif.androidgenericadapter.utils.Constants.CRASHLYTICS_KEY_TAG
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class AppController : Application() {

    override fun onCreate() {
        super.onCreate()
        // Set up Crashlytics, disabled for debug builds
        Builder().core(CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()).build()
        // Init Timber
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }
        // Initialize Koin
        startKoin {
            androidContext(this@AppController)
            modules(appModule, networkModule, repositoryModule, viewModelModule)
        }
    }
}

/** A tree which logs important information for crash reporting.  */
private class CrashReportingTree : Timber.Tree() {

    override fun log(
        priority: Int,
        @Nullable tag: String?,
        @Nullable message: String,
        @Nullable t: Throwable?
    ) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO) {
            return
        }
        setInt(CRASHLYTICS_KEY_PRIORITY, priority)
        setString(CRASHLYTICS_KEY_TAG, tag)
        setString(CRASHLYTICS_KEY_MESSAGE, message)
        t?.let { logException(Exception(message)) } ?: logException(t)
    }
}
