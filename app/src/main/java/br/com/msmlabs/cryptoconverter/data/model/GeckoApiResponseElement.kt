package br.com.msmlabs.cryptoconverter.data.model

import com.google.gson.annotations.SerializedName

typealias GeckoApiResponse = ArrayList<GeckoApiResponseElement>

data class GeckoApiResponseElement(
    val id: String,

    @SerializedName("current_price")
    val currentPrice: Double,

    @SerializedName("high_24h")
    val high24H: Double,

    @SerializedName("low_24h")
    val low24H: Double
)