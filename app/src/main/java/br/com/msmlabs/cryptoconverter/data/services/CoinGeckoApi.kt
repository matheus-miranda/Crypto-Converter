package br.com.msmlabs.cryptoconverter.data.services

import br.com.msmlabs.cryptoconverter.data.model.GeckoApiResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface CoinGeckoApi {

    @GET("{fiat}&ids={crypto}")
    suspend fun exchangeValue(
        @Path("fiat") fiat: String,
        @Path("crypto") crypto: String
    ): GeckoApiResponse
}