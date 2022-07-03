package com.example.stockmarketcompose.presentation.company_info.viewmodel

import com.example.stockmarketcompose.domain.model.CompanyInfo
import com.example.stockmarketcompose.domain.model.IntradayInfo

data class CompanyInfoState(
    val stockInfos : List<IntradayInfo> = emptyList(),
    val company : CompanyInfo? = null,
    val isLoading : Boolean = false,
    val error : String? = null
)
