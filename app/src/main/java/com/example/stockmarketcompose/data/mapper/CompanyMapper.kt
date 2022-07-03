package com.example.stockmarketcompose.data.mapper

import com.example.stockmarketcompose.data.local.CompanyListingEntity
import com.example.stockmarketcompose.data.remote.dto.CompanyInfoDto
import com.example.stockmarketcompose.domain.model.CompanyInfo
import com.example.stockmarketcompose.domain.model.CompanyListing

fun CompanyListingEntity.toCompanyListing() : CompanyListing {
    return CompanyListing(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}

fun CompanyListing.toCompanyListingEntity() : CompanyListingEntity {
    return CompanyListingEntity(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}

fun CompanyInfoDto.toCompanyInfo() : CompanyInfo {
    return CompanyInfo(
        symbol ?: "",
        description ?: "",
        name?: "",
        country?: "",
        industry?: ""

    )
}