package com.example.stockmarketcompose.di

import com.example.stockmarketcompose.data.csv.CSVParser
import com.example.stockmarketcompose.data.csv.CompanyListingParser
import com.example.stockmarketcompose.data.csv.IntradayInfoParser
import com.example.stockmarketcompose.data.repository.StockRepositoryImpl
import com.example.stockmarketcompose.domain.model.CompanyListing
import com.example.stockmarketcompose.domain.model.IntradayInfo
import com.example.stockmarketcompose.domain.repository.StockRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCompanyListingsParser(
        companyListingsParser: CompanyListingParser
    ) : CSVParser<CompanyListing>

    @Binds
    @Singleton
    abstract fun bindIntradayInfoParser(
        intradayInfoParser: IntradayInfoParser
    ) : CSVParser<IntradayInfo>

    @Binds
    @Singleton
    abstract fun bindStockRepository(
       stockRepositoryImpl: StockRepositoryImpl
    ) : StockRepository
}