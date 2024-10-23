package com.example.stocktrackingapk.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @GET("users")
    suspend fun login(): Response<List<SignupResponse>>

    @POST("users")
    suspend fun signup(
        @Body signupRequest: SignupRequest
    ): Response<SignupResponse>
}


interface AlphaVantageApi {
    @GET("query")
    suspend fun getStockData(
        @Query("function") function: String = "TIME_SERIES_INTRADAY",
        @Query("symbol") symbol: String,
        @Query("interval") interval: String = "1min",
        @Query("apikey") apiKey: String
    ): Response<StockResponse>


}
