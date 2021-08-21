package br.com.msmlabs.cryptoconverter.data.repositoryimpl

import br.com.msmlabs.cryptoconverter.data.model.GeckoApiResponse
import br.com.msmlabs.cryptoconverter.data.services.CoinGeckoApi
import br.com.msmlabs.cryptoconverter.domain.repository.CryptoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CryptoRepositoryImpl(private val service: CoinGeckoApi) : CryptoRepository {

    /**
     * Get the result from the API and emit it
     */
    override suspend fun getExchangeValue(fiat: String, crypto: String): Flow<GeckoApiResponse> =
        flow {
            val exchangeValue = service.exchangeValue(fiat, crypto)
            emit(exchangeValue)
        }
}