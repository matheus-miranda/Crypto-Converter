package br.com.msmlabs.cryptoconverter.data.repositoryimpl

import br.com.msmlabs.cryptoconverter.data.model.ErrorResponse
import br.com.msmlabs.cryptoconverter.data.model.GeckoApiResponse
import br.com.msmlabs.cryptoconverter.data.model.GeckoApiResponseElement
import br.com.msmlabs.cryptoconverter.data.model.GeckoResponseEntity
import br.com.msmlabs.cryptoconverter.domain.repository.CryptoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeCryptoRepositoryImpl : CryptoRepository {

    private val geckoResponseItemList = mutableListOf<GeckoResponseEntity>()
    private val emitGeckoResponseEntity = flow {
        emit(geckoResponseItemList)
    }

    private val geckoApiResponse =
        GeckoApiResponseElement(id = "btc", currentPrice = 200.0, high24H = 300.0, low24H = 100.0)

    private var shouldReturnNetworkError = false

    fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    override suspend fun getExchangeValue(fiat: String, crypto: String): Flow<GeckoApiResponse> =
        flow {
            if (shouldReturnNetworkError) {
                ErrorResponse("Network Error")
            } else {
                emit(arrayListOf(geckoApiResponse))
            }
        }

    override fun getAllFromDb(): Flow<List<GeckoResponseEntity>> {
        return emitGeckoResponseEntity
    }

    override suspend fun saveToDb(entity: GeckoResponseEntity) {
        geckoResponseItemList.add(entity)
    }

    override suspend fun deleteAllFromDb() {
        geckoResponseItemList.clear()
    }
}