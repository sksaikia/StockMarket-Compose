package com.example.stockmarketcompose.data.repository

import com.example.stockmarketcompose.data.csv.CSVParser
import com.example.stockmarketcompose.data.csv.CompanyListingParser
import com.example.stockmarketcompose.data.local.StockDatabase
import com.example.stockmarketcompose.data.mapper.toCompanyInfo
import com.example.stockmarketcompose.data.mapper.toCompanyListing
import com.example.stockmarketcompose.data.mapper.toCompanyListingEntity
import com.example.stockmarketcompose.data.remote.StockApi
import com.example.stockmarketcompose.domain.model.CompanyInfo
import com.example.stockmarketcompose.domain.model.CompanyListing
import com.example.stockmarketcompose.domain.model.IntradayInfo
import com.example.stockmarketcompose.domain.repository.StockRepository
import com.example.stockmarketcompose.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    val api : StockApi,
    val db : StockDatabase,
    val companyListingParser: CSVParser<CompanyListing>,
    val intradayInfoParser: CSVParser<IntradayInfo>
) : StockRepository {

    override suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>> {

        return flow {
           emit(Resource.Loading(true))
           val localListings = db.dao.searchCompanyListing(query)
           emit(Resource.Success(
               data = localListings.map { companyListingEntity ->
                   companyListingEntity.toCompanyListing()
               }
           ))

            val isDbEmpty = localListings.isEmpty() && query.isBlank()
            val shouldJustLoadFromCache = !isDbEmpty && !fetchFromRemote
            if (shouldJustLoadFromCache) {
                emit(Resource.Loading(false))
                return@flow
            }

            val remoteListings = try {
                val response = api.getListings()
                companyListingParser.parse(response.byteStream())
            } catch (e : IOException) {
                e.printStackTrace()
                emit(Resource.Error("Could not load data"))
                null
            } catch (e : HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Could not load data"))
                null
            }

            remoteListings?.let { listings ->
                db.dao.clearCompanyListings()
                db.dao.insertCompanyListings(
                    listings.map { it.toCompanyListingEntity() }
                )
                emit(Resource.Success(data = db.dao.searchCompanyListing("").map { it.toCompanyListing() }))
                emit(Resource.Loading(true))
            }

        }
    }

    override suspend fun getIntradayInfo(symbol: String): Resource<List<IntradayInfo>> {
        return try {
            val response = api.getIntradayInfo(symbol)
            val results = intradayInfoParser.parse(response.byteStream())
            Resource.Success(results)

        } catch (e : IOException) {
            e.printStackTrace()
            Resource.Error(
                "Could not get data"
            )
        } catch (e : HttpException) {
            e.printStackTrace()
            Resource.Error(
                "Could not get data ${e.code()}"
            )
        }
    }

    override suspend fun getCompanyInfo(symbol: String): Resource<CompanyInfo> {
        return try {
            val result = api.getCompanyInfo(symbol)
            Resource.Success(result.toCompanyInfo())
        } catch (e : IOException) {
            e.printStackTrace()
            Resource.Error(
                "Could not get data"
            )
        } catch (e : HttpException) {
            e.printStackTrace()
            Resource.Error(
                "Could not get data ${e.code()}"
            )
        }
    }

}