package com.danhdueexoictif.androidgenericadapter.di

import android.content.Context
import android.content.SharedPreferences
import com.danhdueexoictif.androidgenericadapter.BuildConfig
import com.danhdueexoictif.androidgenericadapter.data.local.pref.AppSharedPreferenceHelper
import com.danhdueexoictif.androidgenericadapter.data.local.pref.SharedPreferenceHelper
import com.danhdueexoictif.androidgenericadapter.data.local.pref.SharedPrefsApi
import com.danhdueexoictif.androidgenericadapter.utils.Constants
import com.danhdueexoictif.androidgenericadapter.utils.analytic.LoggingHelper
import com.danhdueexoictif.androidgenericadapter.utils.analytic.LoggingUtils
import com.danhdueexoictif.androidgenericadapter.utils.brightness.BrightnessHelper
import com.danhdueexoictif.androidgenericadapter.utils.brightness.BrightnessUtils
import com.danhdueexoictif.androidgenericadapter.utils.controller.ViewModelObservable
import com.danhdueexoictif.androidgenericadapter.utils.controller.ViewModelObservableImpl
import com.danhdueexoictif.androidgenericadapter.utils.controller.ViewObservable
import com.danhdueexoictif.androidgenericadapter.utils.controller.ViewObservableImpl
import com.danhdueexoictif.androidgenericadapter.utils.locale.AppLocaleHelper
import com.danhdueexoictif.androidgenericadapter.utils.locale.LocaleHelper
import com.danhdueexoictif.androidgenericadapter.utils.sharedpreference.LiveSharedPreferences
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.upstream.cache.Cache
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import im.ene.toro.exoplayer.Config
import im.ene.toro.exoplayer.MediaSourceBuilder
import im.ene.toro.exoplayer.ToroExo
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File

val appModule = module {
    single { androidApplication().resources }
    single { androidApplication().assets }
    single { Gson() }
    single { LiveSharedPreferences(get()) }
    single { SharedPrefsApi(get()) }
    single<SharedPreferences> {
        androidApplication().getSharedPreferences(
            BuildConfig.SHARED_PREFERENCES_NAME,
            Context.MODE_PRIVATE
        )
    }
    single<SharedPreferenceHelper> { AppSharedPreferenceHelper(get()) }
    single<LocaleHelper> { AppLocaleHelper(get()) }
    single<LoggingHelper> { LoggingUtils(FirebaseAnalytics.getInstance(androidContext()), get()) }
    single<ViewModelObservable> { ViewModelObservableImpl() }
    single<ViewObservable> { ViewObservableImpl() }
    single<BrightnessHelper> { BrightnessUtils.init(get()) }

    /**
     *  provide ExoPlayer Cache
     */
    single<Cache>(qualifier = named("exoCache")) {
        SimpleCache(
            File(androidContext().filesDir.path + Constants.VIDEO_CACHE_PATH),
            LeastRecentlyUsedCacheEvictor(2 * 1024 * 1024L), ExoDatabaseProvider(androidContext())
        )
    }

    /**
     * provide ExoPlayer Config
     */
    single {
        Config.Builder(androidContext()).setMediaSourceBuilder(MediaSourceBuilder.DEFAULT)
            .setCache(get(qualifier = named("exoCache"))).build()
    }

    /**
     * provide ExoCreator
     */
    single { ToroExo.with(androidContext()).getCreator(get()) }
}
