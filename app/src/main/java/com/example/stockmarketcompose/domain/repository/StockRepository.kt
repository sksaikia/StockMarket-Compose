package com.example.stockmarketcompose.domain.repository

import com.example.stockmarketcompose.domain.model.CompanyInfo
import com.example.stockmarketcompose.domain.model.CompanyListing
import com.example.stockmarketcompose.domain.model.IntradayInfo
import com.example.stockmarketcompose.util.Resource
import kotlinx.coroutines.flow.Flow

interface StockRepository {

    suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ) : Flow<Resource<List<CompanyListing>>>

    suspend fun getIntradayInfo(
        symbol : String
    ) : Resource<List<IntradayInfo>>


    suspend fun getCompanyInfo(
        symbol: String
    ) : Resource<CompanyInfo>
}