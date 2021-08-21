package br.com.msmlabs.cryptoconverter.domain.usecase

import br.com.msmlabs.cryptoconverter.data.model.GeckoAPIResponse
import br.com.msmlabs.cryptoconverter.domain.repository.CryptoRepository
import kotlinx.coroutines.flow.Flow

class GetExchangeValueUseCase(private val repository: CryptoRepository) {

    data class Params(
        val fiat: String,
        val crypto: String
    )

    suspend fun execute(params: Params): Flow<GeckoAPIResponse> {
        return repository.getExchangeValue(params.fiat, params.crypto)
    }
}