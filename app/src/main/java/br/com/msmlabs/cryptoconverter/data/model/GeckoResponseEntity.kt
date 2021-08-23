package br.com.msmlabs.cryptoconverter.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_exchange")
data class GeckoResponseEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    val types: String,

    val currentPrice: Double
)
