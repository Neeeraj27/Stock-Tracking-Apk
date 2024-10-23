package com.example.stocktrackingapk.ui.StockScreen

import android.util.Log
import com.example.stocktrackingapk.data.AlphaVantageApi
import com.example.stocktrackingapk.data.StockResponse
import javax.inject.Inject

class StockRepository @Inject constructor(
    private val api: AlphaVantageApi
) {
    suspend fun getStockData(symbol: String): Result<StockResponse?> {
        return try {
            val response = api.getStockData(
                symbol = symbol,
                apiKey = "Q6VEQDNXFDR8DTVA"
            )

            Log.e("Response", "Response  $response")

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body())

            } else {
                val errorMessage = "Failed to fetch stock data: ${response.errorBody()?.string() ?: "Unknown error"}"
                Log.e("StockRepository", errorMessage)
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Log.e("StockRepository", "Exception occurred while fetching stock data", e)
            Result.failure(e)
        }
    }

}