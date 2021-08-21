package br.com.msmlabs.cryptoconverter.domain.di

import br.com.msmlabs.cryptoconverter.domain.usecase.GetExchangeValueUseCase
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

object DomainModule {

    fun load() {
        loadKoinModules(useCaseModule())
    }

    private fun useCaseModule(): Module {

        return module {

            factory {
                GetExchangeValueUseCase(repository = get())
            }
        }
    }
}