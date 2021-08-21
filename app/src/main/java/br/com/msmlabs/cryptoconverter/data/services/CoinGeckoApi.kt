package br.com.msmlabs.cryptoconverter.data.services

import retrofit2.http.GET
import retrofit2.http.Path

interface CoinGeckoApi {

    @GET("{fiat}&ids={crypto}")
    suspend fun exchangeValue(@Path("fiat") fiat: String, @Path("crypto") crypto: String)
}