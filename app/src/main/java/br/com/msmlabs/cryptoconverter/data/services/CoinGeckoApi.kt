package br.com.msmlabs.cryptoconverter.data.services

import br.com.msmlabs.cryptoconverter.data.model.GeckoAPIResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CoinGeckoApi {

    @GET("markets?")
    suspend fun exchangeValue(
        @Query("vs_currency") fiat: String, @Query("ids") crypto: String
    ): GeckoAPIResponse
}