package com.danhdueexoictif.androidgenericadapter.di

import com.danhdueexoictif.androidgenericadapter.data.repository.AuthRepository
import com.danhdueexoictif.androidgenericadapter.data.repository.SampleRepository
import com.danhdueexoictif.androidgenericadapter.data.repository.UserRepository
import com.danhdueexoictif.androidgenericadapter.data.repository.impl.AuthRepositoryImpl
import com.danhdueexoictif.androidgenericadapter.data.repository.impl.SampleRepositoryImpl
import com.danhdueexoictif.androidgenericadapter.data.repository.impl.UserRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<SampleRepository> { SampleRepositoryImpl(get(), get()) }
}
