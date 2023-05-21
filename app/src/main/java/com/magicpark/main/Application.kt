package com.magicpark.app

import android.app.Application

import com.magicpark.core.di.AppModule
import dagger.hilt.android.HiltAndroidApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent


@HiltAndroidApp
class MainApplication : Application() {



    override fun onCreate() {
        super.onCreate()



        startKoin {
            androidContext(this@MainApplication)
            androidLogger()

            modules(AppModule)
        }

    }

}