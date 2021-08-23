package br.com.msmlabs.cryptoconverter.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.msmlabs.cryptoconverter.data.model.GeckoResponseEntity

@Database(entities = [GeckoResponseEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        fun getInstance(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "db_crypto"
            ).fallbackToDestructiveMigration().build()
        }
    }
}