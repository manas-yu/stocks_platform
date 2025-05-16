package com.example.stock_platform.di

import android.app.Application
import com.example.stock_platform.data.manager.LocalUserManagerImpl
import com.example.stock_platform.data.remote.StocksApi
import com.example.stock_platform.data.repository.StocksRepositoryImpl
import com.example.stock_platform.domain.manager.LocalUserManager
import com.example.stock_platform.domain.repository.StocksRepository
import com.example.stock_platform.domain.usecases.app_entry.AppEntryUseCases
import com.example.stock_platform.domain.usecases.app_entry.ReadUserApi
import com.example.stock_platform.domain.usecases.app_entry.RemoveUserApi
import com.example.stock_platform.domain.usecases.app_entry.SaveUserApi
import com.example.stock_platform.domain.usecases.stocks.StockUseCases
import com.example.stock_platform.domain.usecases.stocks.gainers_losers.GetTopGainersLosers
import com.example.stock_platform.domain.usecases.stocks.overview.GetCompanyOverview
import com.example.stock_platform.domain.usecases.stocks.search.GetSearchList
import com.example.stock_platform.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideLocalUserManager(application: Application): LocalUserManager {
        return LocalUserManagerImpl(application)
    }

    @Provides
    @Singleton
    fun provideAppEntryUseCases(localUserManager: LocalUserManager) = AppEntryUseCases(
        saveUserApi = SaveUserApi(localUserManager),
        removeUserApi = RemoveUserApi(localUserManager),
        readUserApi = ReadUserApi(localUserManager)
    )
    @Provides
    @Singleton
    fun provideStocksApi():StocksApi{
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(StocksApi::class.java)
    }
    @Provides
    @Singleton
    fun provideStocksRepository(
        stocksApi: StocksApi
    ):StocksRepository=StocksRepositoryImpl(stocksApi)

    @Provides
    @Singleton
    fun provideStocksUseCase(
        stocksRepository: StocksRepository
    ): StockUseCases {
        return StockUseCases(
            getTopGainersLosers = GetTopGainersLosers(stocksRepository),
            getCompanyOverview = GetCompanyOverview(stocksRepository),
            getSearchList = GetSearchList(stocksRepository)
        )
    }

}