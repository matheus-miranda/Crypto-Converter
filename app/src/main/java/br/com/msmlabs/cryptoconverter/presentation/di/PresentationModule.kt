package br.com.msmlabs.cryptoconverter.presentation.di

import br.com.msmlabs.cryptoconverter.presentation.viewmodel.HistoryViewModel
import br.com.msmlabs.cryptoconverter.presentation.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

object PresentationModule {

    fun load() {
        loadKoinModules(viewModelModules())
    }

    private fun viewModelModules(): Module {

        return module {

            viewModel {
                HomeViewModel(getValueUseCase = get(), saveToDbUseCase = get())
            }

            viewModel {
                HistoryViewModel(listAllUseCase = get())
            }
        }
    }
}