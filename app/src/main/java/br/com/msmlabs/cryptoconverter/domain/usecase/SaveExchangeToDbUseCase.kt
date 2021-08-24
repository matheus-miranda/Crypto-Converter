package br.com.msmlabs.cryptoconverter.domain.usecase

import br.com.msmlabs.cryptoconverter.data.model.GeckoResponseEntity
import br.com.msmlabs.cryptoconverter.domain.repository.CryptoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SaveExchangeToDbUseCase(
    private val repository: CryptoRepository
) : UseCase.NoSource<GeckoResponseEntity>() {

    override suspend fun execute(param: GeckoResponseEntity): Flow<Unit> {
        return flow {
            repository.saveToDb(param)
            emit(Unit)
        }
    }
}