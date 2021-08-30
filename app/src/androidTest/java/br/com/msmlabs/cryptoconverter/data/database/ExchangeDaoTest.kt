package br.com.msmlabs.cryptoconverter.data.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import br.com.msmlabs.cryptoconverter.data.model.GeckoResponseEntity
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class ExchangeDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule() // Needed due to asynchronous code

    private lateinit var database: AppDatabase
    private lateinit var dao: ExchangeDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.exchangeDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun saveSearchToDb() = runBlockingTest {
        val searchQuery = GeckoResponseEntity(id = 1L, types = "btc/brl", currentPrice = "$1000")
        dao.saveToDb(searchQuery)

        val allSearchResponses = dao.getAllFromDb().first()

        assertThat(allSearchResponses).contains(searchQuery)
    }

    @Test
    fun deleteAllFromDb() = runBlockingTest {
        val searchQuery = GeckoResponseEntity(id = 1L, types = "btc/brl", currentPrice = "$1000")
        dao.saveToDb(searchQuery)

        dao.deleteAllFromDb()
        val allSearchResponses = dao.getAllFromDb().first()

        assertThat(allSearchResponses).doesNotContain(searchQuery)
    }
}