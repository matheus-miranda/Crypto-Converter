package br.com.msmlabs.cryptoconverter.data.model

import com.google.gson.annotations.SerializedName

typealias GeckoAPIResponse = ArrayList<GeckoAPIResponseElement>

data class GeckoAPIResponseElement(
    val id: String,

    @SerializedName("current_price")
    val currentPrice: Double,

    @SerializedName("high_24h")
    val high24H: Double,

    @SerializedName("low_24h")
    val low24H: Double
)