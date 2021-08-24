package br.com.msmlabs.cryptoconverter.domain.usecase

import br.com.msmlabs.cryptoconverter.domain.repository.CryptoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DeleteAllFromDbUseCase(private val repository: CryptoRepository) : UseCase.NoParamOrSource() {

    override suspend fun execute(param: None): Flow<None> {
        return flow {
            repository.deleteAllFromDb()
        }
    }
}