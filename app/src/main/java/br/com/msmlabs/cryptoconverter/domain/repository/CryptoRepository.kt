package br.com.msmlabs.cryptoconverter.domain.repository

import br.com.msmlabs.cryptoconverter.data.model.GeckoApiResponse
import br.com.msmlabs.cryptoconverter.data.model.GeckoResponseEntity
import kotlinx.coroutines.flow.Flow

interface CryptoRepository {

    suspend fun getExchangeValue(fiat: String, crypto: String): Flow<GeckoApiResponse>

    fun getAllFromDb(): Flow<List<GeckoResponseEntity>>

    suspend fun saveToDb(entity: GeckoResponseEntity)

    suspend fun deleteAllFromDb()
}