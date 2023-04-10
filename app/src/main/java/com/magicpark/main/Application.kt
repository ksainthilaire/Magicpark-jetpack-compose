package com.magicpark.app

import android.app.Application
import com.magicpark.core.di.dataModule
import com.magicpark.core.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApplication)
            androidLogger()

            modules(dataModule)
            modules(domainModule)
        }
    }
}