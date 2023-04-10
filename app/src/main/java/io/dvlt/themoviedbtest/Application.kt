package io.dvlt.themoviedbtest

import android.app.Application
import io.dvlt.core.di.dataModule
import io.dvlt.core.di.domainModule
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