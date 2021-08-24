package br.com.msmlabs.cryptoconverter.data.repositoryimpl

import br.com.msmlabs.cryptoconverter.core.exceptions.RemoteException
import br.com.msmlabs.cryptoconverter.data.database.AppDatabase
import br.com.msmlabs.cryptoconverter.data.model.ErrorResponse
import br.com.msmlabs.cryptoconverter.data.model.GeckoApiResponse
import br.com.msmlabs.cryptoconverter.data.model.GeckoResponseEntity
import br.com.msmlabs.cryptoconverter.data.services.CoinGeckoApi
import br.com.msmlabs.cryptoconverter.domain.repository.CryptoRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class CryptoRepositoryImpl(
    private val service: CoinGeckoApi,
    database: AppDatabase
) : CryptoRepository {

    private val dao = database.exchangeDao()

    /**
     * Get the result from the API and emit it
     */
    override suspend fun getExchangeValue(fiat: String, crypto: String): Flow<GeckoApiResponse> =
        flow {
            try {
                val exchangeValue = service.exchangeValue(fiat, crypto)
                emit(exchangeValue)
            } catch (e: HttpException) {
                val json = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(json, ErrorResponse::class.java)
                throw RemoteException(errorResponse.error)
            }
        }

    override fun getAllFromDb(): Flow<List<GeckoResponseEntity>> {
        return dao.getAllFromDb()
    }

    override suspend fun saveToDb(entity: GeckoResponseEntity) {
        dao.saveToDb(entity)
    }

    override suspend fun deleteAllFromDb() {
        dao.deleteAllFromDb()
    }
}