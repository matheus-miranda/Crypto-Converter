package br.com.msmlabs.cryptoconverter.data.di

import android.util.Log
import br.com.msmlabs.cryptoconverter.data.services.CoinGeckoApi
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DataModules {

    private const val HTTP_TAG = "okHttp"

    /**
     * Loads all dependencies for the Data module
     */
    fun load() {
        loadKoinModules(networkModule())
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

    private inline fun <reified T> createService(
        client: OkHttpClient,
        factory: GsonConverterFactory
    ): T {
        return Retrofit.Builder()
            .baseUrl("https://api.coingecko.com/api/v3/coins/markets?vs_currency=")
            .client(client)
            .addConverterFactory(factory)
            .build()
            .create(T::class.java)
    }
}