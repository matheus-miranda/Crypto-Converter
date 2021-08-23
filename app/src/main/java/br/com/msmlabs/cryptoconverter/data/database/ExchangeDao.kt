package br.com.msmlabs.cryptoconverter.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.msmlabs.cryptoconverter.data.model.GeckoResponseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExchangeDao {

    @Query("SELECT * FROM tb_exchange")
    fun getAllFromDb(): Flow<List<GeckoResponseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveToDb(entity: GeckoResponseEntity)

    @Query("DELETE FROM tb_exchange")
    suspend fun deleteAllFromDb()
}