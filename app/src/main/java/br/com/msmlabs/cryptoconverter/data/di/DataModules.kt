package br.com.msmlabs.cryptoconverter.data.di

import android.util.Log
import br.com.msmlabs.cryptoconverter.data.database.AppDatabase
import br.com.msmlabs.cryptoconverter.data.repositoryimpl.CryptoRepositoryImpl
import br.com.msmlabs.cryptoconverter.data.services.CoinGeckoApi
import br.com.msmlabs.cryptoconverter.domain.repository.CryptoRepository
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DataModules {

    private const val HTTP_TAG = "okHttp"
    private const val BASE_URL = "https://api.coingecko.com/api/v3/coins/"

    /**
     * Loads all dependencies for the Data module
     */
    fun load() {
        loadKoinModules(networkModule() + repositoryModule() + databaseModule())
    }

    private fun networkModule(): Module {

        return module {

            // OkHttpClient
            single {
                val interceptor = HttpLoggingInterceptor {
                    Log.e(HTTP_TAG, ": $it")
                }
                interceptor.level = HttpLoggingInterceptor.Level.BODY

                OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build()
            }

            single {
                GsonConverterFactory.create(GsonBuilder().create())
            }

            single {
                createService<CoinGeckoApi>(client = get(), factory = get())
            }
        }
    }

    private fun repositoryModule(): Module {

        return module {
            factory<CryptoRepository> {
                CryptoRepositoryImpl(service = get())
            }
        }
    }

    private fun databaseModule(): Module {

        return module {
            single {
                AppDatabase.getInstance(context = androidApplication())
            }
        }
    }

    private inline fun <reified T> createService(
        client: OkHttpClient,
        factory: GsonConverterFactory
    ): T {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(factory)
            .build()
            .create(T::class.java)
    }
}