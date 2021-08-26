package br.com.msmlabs.cryptoconverter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.msmlabs.cryptoconverter.data.model.GeckoApiResponse
import br.com.msmlabs.cryptoconverter.data.model.GeckoResponseEntity
import br.com.msmlabs.cryptoconverter.domain.usecase.GetExchangeValueUseCase
import br.com.msmlabs.cryptoconverter.domain.usecase.SaveExchangeToDbUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getValueUseCase: GetExchangeValueUseCase,
    private val saveToDbUseCase: SaveExchangeToDbUseCase
) : ViewModel() {

    private val _state = MutableLiveData<State>()
    val state: LiveData<State> = _state

    // Indicates if the array adapters were swapped
    var swapped: Boolean = false

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

    fun saveToDb(entity: GeckoResponseEntity) {
        viewModelScope.launch {
            saveToDbUseCase(entity)
                .flowOn(Dispatchers.Main)
                .onStart {
                    _state.value = State.Loading
                }
                .catch {
                    _state.value = State.Error(it)
                }
                .collect {
                    _state.value = State.Saved
                }
        }
    }

    sealed class State {
        object Loading : State()
        object Saved : State()

        data class Success(val exchangeValue: GeckoApiResponse) : State()
        data class Error(val throwable: Throwable) : State()
    }
}