package br.com.msmlabs.cryptoconverter.domain.repository

import br.com.msmlabs.cryptoconverter.data.model.GeckoAPIResponse
import kotlinx.coroutines.flow.Flow

interface CryptoRepository {

    suspend fun getExchangeValue(fiat: String, crypto: String): Flow<GeckoAPIResponse>
}