package com.example.stocktrackingapk.data

import com.google.gson.annotations.SerializedName


data class SignupRequest(
    val id: String,
    val email: String,
    val password: String
)


data class SignupResponse(
    val id: String,
    val email: String,
    val password: String
)

data class StockResponse(
    @SerializedName("Meta Data")
    val metaData: MetaData?,
    @SerializedName("Time Series (1min)")
    val timeSeries: Map<String, TimeSeriesData>?
)

data class MetaData(
    @SerializedName("1. Information")
    val information: String?,
    @SerializedName("2. Symbol")
    val symbol: String?,
    @SerializedName("3. Last Refreshed")
    val lastRefreshed: String?
)

data class TimeSeriesData(
    @SerializedName("1. open")
    val open: String?,
    @SerializedName("2. high")
    val high: String?,
    @SerializedName("3. low")
    val low: String?,
    @SerializedName("4. close")
    val close: String?,
    @SerializedName("5. volume")
    val volume: String?
)
