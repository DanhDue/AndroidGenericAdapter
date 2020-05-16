package com.danhdueexoictif.androidgenericadapter.di

import com.danhdueexoictif.androidgenericadapter.ui.base.SharedViewModel
import com.danhdueexoictif.androidgenericadapter.ui.screen.home.HomeViewModel
import com.danhdueexoictif.androidgenericadapter.ui.screen.login.LoginViewModel
import com.danhdueexoictif.androidgenericadapter.ui.screen.menu.MenuViewModel
import com.danhdueexoictif.androidgenericadapter.ui.screen.sample.SampleViewModel
import com.danhdueexoictif.androidgenericadapter.ui.screen.splash.SplashViewModel
import com.danhdueexoictif.androidgenericadapter.ui.widgets.forcedupgrade.ForcedUpgradeDialogViewModel
import com.danhdueexoictif.androidgenericadapter.ui.widgets.oopsnointernet.OopsNoInternetDialogViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SharedViewModel() }
    viewModel { HomeViewModel() }
    viewModel { LoginViewModel() }
    viewModel { MenuViewModel() }
    viewModel { SampleViewModel((get())) }
    viewModel { SplashViewModel() }
    viewModel { ForcedUpgradeDialogViewModel() }
    viewModel { OopsNoInternetDialogViewModel() }
}
