package br.com.msmlabs.cryptoconverter

import android.app.Application
import br.com.msmlabs.cryptoconverter.data.di.DataModules
import br.com.msmlabs.cryptoconverter.domain.di.DomainModule
import br.com.msmlabs.cryptoconverter.presentation.di.PresentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
        }

        DataModules.load()
        DomainModule.load()
        PresentationModule.load()
    }
}