package com.example.stocktrackingapk.di

import android.content.Context
import com.example.stocktrackingapk.data.AlphaVantageApi
import com.example.stocktrackingapk.data.ApiService
import com.example.stocktrackingapk.data.AuthRepository
import com.example.stocktrackingapk.ui.StockScreen.StockRepository
import com.example.stocktrackingapk.utils.PrefManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // Provides the ApiService which interacts with the mock API for user-related operations (login/signup)
    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl("https://6712ba8a6c5f5ced66247e84.mockapi.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
    // Provides the AlphaVantageApi, which fetches stock data from Alpha Vantage's API
    @Provides
    @Singleton
    fun provideAlphaVantageApi(): AlphaVantageApi {
        return Retrofit.Builder()
            .baseUrl("https://www.alphavantage.co/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AlphaVantageApi::class.java)
    }

    @Provides
    @Singleton
    fun providePrefManager(@ApplicationContext context: Context): PrefManager {
        return PrefManager(context)
    }

    @Provides
    fun provideAuthRepository(apiService: ApiService): AuthRepository {
        return AuthRepository(apiService)
    }

    @Provides
    fun provideStockRepository(api: AlphaVantageApi): StockRepository {
        return StockRepository(api)
    }


}
