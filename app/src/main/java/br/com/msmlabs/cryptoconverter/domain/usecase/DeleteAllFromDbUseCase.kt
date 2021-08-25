package br.com.msmlabs.cryptoconverter.domain.usecase

import br.com.msmlabs.cryptoconverter.domain.repository.CryptoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DeleteAllFromDbUseCase(private val repository: CryptoRepository) {

    suspend fun execute(): Flow<Unit> {
        return flow {
            repository.deleteAllFromDb()
            emit(Unit)
        }
    }
}