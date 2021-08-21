package br.com.msmlabs.cryptoconverter

import android.app.Application
import br.com.msmlabs.cryptoconverter.data.di.DataModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
        }

        DataModules.load()
    }
}