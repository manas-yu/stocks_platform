package com.example.stock_platform.di

import android.app.Application
import androidx.room.Room
import com.example.stock_platform.data.local.GainersLosersDao
import com.example.stock_platform.data.local.OverviewDao
import com.example.stock_platform.data.local.SearchDao
import com.example.stock_platform.data.local.StocksDatabase
import com.example.stock_platform.data.local.StocksTypeConvertor
import com.example.stock_platform.data.remote.StocksApi
import com.example.stock_platform.data.repository.StocksRepositoryImpl
import com.example.stock_platform.domain.repository.StocksRepository
import com.example.stock_platform.domain.usecases.stocks.StockUseCases
import com.example.stock_platform.domain.usecases.stocks.gainers_losers.GetMostRecentGainersLosers
import com.example.stock_platform.domain.usecases.stocks.gainers_losers.GetTopGainersLosers
import com.example.stock_platform.domain.usecases.stocks.gainers_losers.UpsertGainersLosers
import com.example.stock_platform.domain.usecases.stocks.overview.DeleteOlderOverviews
import com.example.stock_platform.domain.usecases.stocks.overview.GetCachedOverview
import com.example.stock_platform.domain.usecases.stocks.overview.GetCompanyOverview
import com.example.stock_platform.domain.usecases.stocks.overview.InsertOverview
import com.example.stock_platform.domain.usecases.stocks.search.DeleteOlderSearches
import com.example.stock_platform.domain.usecases.stocks.search.GetRecentSearches
import com.example.stock_platform.domain.usecases.stocks.search.GetSearchList
import com.example.stock_platform.domain.usecases.stocks.search.UpsertSearchEntry
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
    fun provideStocksApi():StocksApi{
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(StocksApi::class.java)
    }

    @Provides
    @Singleton
    fun provideStocksRepository(
        stocksApi: StocksApi,
        searchDao: SearchDao,
        gainersLosersDao: GainersLosersDao,
        overviewDao: OverviewDao
    ):StocksRepository=StocksRepositoryImpl(stocksApi,searchDao,gainersLosersDao,overviewDao)

    @Provides
    @Singleton
    fun provideStocksUseCase(
        stocksRepository: StocksRepository
    ): StockUseCases {
        return StockUseCases(
            getTopGainersLosers = GetTopGainersLosers(stocksRepository),
            getCompanyOverview = GetCompanyOverview(stocksRepository),
            getSearchList = GetSearchList(stocksRepository),
            upsertGainersLosers = UpsertGainersLosers(stocksRepository),
            getMostRecentGainersLosers = GetMostRecentGainersLosers(stocksRepository),
            getRecentSearches = GetRecentSearches(stocksRepository),
            upsertSearchEntry = UpsertSearchEntry(stocksRepository),
            deleteOlderSearches = DeleteOlderSearches(stocksRepository),
            deleteOlderOverviews = DeleteOlderOverviews(stocksRepository),
            insertOverview = InsertOverview(stocksRepository),
            getCachedOverview = GetCachedOverview(stocksRepository)
        )
    }

    @Provides
    @Singleton
    fun provideStocksDatabase(
        application: Application
    ): StocksDatabase {
        return Room.databaseBuilder(
            context = application,
            klass=StocksDatabase::class.java,
            "stocks_database"
        ).addTypeConverter(StocksTypeConvertor()).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideSearchDao(
        stocksDatabase: StocksDatabase
    ): SearchDao = stocksDatabase.searchDao

    @Provides
    @Singleton
    fun provideGainersLosersDao(
        stocksDatabase: StocksDatabase
    ): GainersLosersDao = stocksDatabase.gainersLosersDao

    @Provides
    @Singleton
    fun provideOverviewDao(
        stocksDatabase: StocksDatabase
    ): OverviewDao = stocksDatabase.overviewDao

}