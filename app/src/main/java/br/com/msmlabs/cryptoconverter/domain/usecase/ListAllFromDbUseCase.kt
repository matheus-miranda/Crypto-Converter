package br.com.msmlabs.cryptoconverter.domain.usecase

import br.com.msmlabs.cryptoconverter.data.model.GeckoResponseEntity
import br.com.msmlabs.cryptoconverter.domain.repository.CryptoRepository
import kotlinx.coroutines.flow.Flow

class ListAllFromDbUseCase(
    private val repository: CryptoRepository
) : UseCase.NoParam<List<GeckoResponseEntity>>() {

    override suspend fun execute(): Flow<List<GeckoResponseEntity>> {
        return repository.getAllFromDb()
    }
}