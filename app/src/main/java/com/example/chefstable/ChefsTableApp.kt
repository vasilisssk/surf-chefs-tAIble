package com.example.chefstable

import android.app.Application
import com.example.chefstable.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ChefsTableApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@ChefsTableApp)
            modules(appModule)
        }
    }
}
