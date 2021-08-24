package br.com.msmlabs.cryptoconverter.presentation.viewmodel

import androidx.lifecycle.*
import br.com.msmlabs.cryptoconverter.data.model.GeckoResponseEntity
import br.com.msmlabs.cryptoconverter.domain.usecase.ListAllFromDbUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val listAllUseCase: ListAllFromDbUseCase
) : ViewModel(), LifecycleObserver {

    private val _state = MutableLiveData<State>()
    val state: LiveData<State> = _state

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun getAllExchanges() {

        viewModelScope.launch {
            listAllUseCase()
                .flowOn(Dispatchers.Main)
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

        data class Success(val exchangeValue: List<GeckoResponseEntity>) : State()
        data class Error(val throwable: Throwable) : State()
    }

}