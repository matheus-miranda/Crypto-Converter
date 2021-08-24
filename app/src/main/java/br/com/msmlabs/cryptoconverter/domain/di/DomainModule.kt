package br.com.msmlabs.cryptoconverter.domain.di

import br.com.msmlabs.cryptoconverter.domain.usecase.GetExchangeValueUseCase
import br.com.msmlabs.cryptoconverter.domain.usecase.ListAllFromDbUseCase
import br.com.msmlabs.cryptoconverter.domain.usecase.SaveExchangeToDbUseCase
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

            factory {
                SaveExchangeToDbUseCase(repository = get())
            }

            factory {
                ListAllFromDbUseCase(repository = get())
            }
        }
    }
}