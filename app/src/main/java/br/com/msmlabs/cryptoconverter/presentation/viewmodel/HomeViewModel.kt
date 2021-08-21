package br.com.msmlabs.cryptoconverter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.msmlabs.cryptoconverter.data.model.GeckoAPIResponse
import br.com.msmlabs.cryptoconverter.domain.usecase.GetExchangeValueUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class HomeViewModel(private val getValueUseCase: GetExchangeValueUseCase) : ViewModel() {

    private val _state = MutableLiveData<State>()
    val state: LiveData<State> = _state

    fun getValues(fiat: String, crypto: String) {

        viewModelScope.launch {
            getValueUseCase.execute(
                GetExchangeValueUseCase.Params(
                    fiat = fiat,
                    crypto = crypto
                )
            ).flowOn(Dispatchers.Main)
                .onStart {
                    _state.value = State.Loading
                }
                .catch {
                    _state.value = State.Error(it)
                }
                .collect {
                    _state.value = State.Success(it)
                }
        }
    }

    sealed class State {
        object Loading : State()

        data class Success(val value: GeckoAPIResponse) : State()
        data class Error(val throwable: Throwable) : State()
    }
}