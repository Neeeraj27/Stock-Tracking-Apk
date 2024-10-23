package com.example.stocktrackingapk.ui.StockScreen

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stocktrackingapk.components.WebSocketManager
import com.example.stocktrackingapk.data.StockResponse
import com.example.stocktrackingapk.data.TimeSeriesData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockViewModel @Inject constructor(private val stockRepository: StockRepository) : ViewModel() {

    // State to hold the list of stocks
    private val _stocks = MutableStateFlow<List<StockData>>(emptyList())
    val stocks: StateFlow<List<StockData>> = _stocks

    // State to track the loading status
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    val _showLogoutDialogBox = mutableStateOf(false)
    val showLogoutDialogBox: State<Boolean> = _showLogoutDialogBox

    // Function to fetch stock data for a list of symbols
    fun fetchStocks(symbols: List<String>, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            val fetchedStocks = mutableListOf<StockData>()

            // Mock data to be used when API limit is exceeded or any issue occurs
            val mockStocks = listOf(
                StockData(name = "AAPL", displayName = "Apple", price = 145.09f, change = "+1.34%"),
                StockData(name = "GOOGL", displayName = "Google", price = 2731.60f, change = "-0.45%"),
                StockData(name = "MSFT", displayName = "Microsoft", price = 299.45f, change = "+2.87%"),
                StockData(name = "AMZN", displayName = "Amazon", price = 167.74f, change = "0.12%"),
                StockData(name = "META", displayName = "Meta", price = 123.74f, change = "-0.96%"),
                StockData(name = "TSLA", displayName = "Tesla", price = 58.74f, change = "+0.56%"),
                StockData(name = "ORCL", displayName = "Oracle", price = 40.74f, change = "+0.65%"),
                StockData(name = "V", displayName = "Visa", price = 3420.74f, change = "-0.47%"),
                StockData(name = "UNH", displayName = "UnitedHealth", price = 70.74f, change = "-0.11%"),
                StockData(name = "JPM", displayName = "JPMorgan", price = 53.74f, change = "+0.48%"),
                StockData(name = "AVGO", displayName = "Broadcom", price = 3420.74f, change = "-0.29%"),
                StockData(name = "MA", displayName = "Mastercard", price = 39.74f, change = "-0.35%")
            )

            var useMockData = false

            try {
                // Launch coroutines for each symbol to make requests concurrently
                val deferredResults = symbols.map { symbol ->
                    async {
                        try {
                            stockRepository.getStockData(symbol)
                        } catch (e: Exception) {
                            Log.e("StockViewModel", "Exception caught for $symbol", e)
                            null
                        }
                    }
                }

                // Await the completion of all requests
                val results = deferredResults.awaitAll()

                for ((index, response) in results.withIndex()) {
                    val symbol = symbols[index]

                    if (response != null && response.isSuccess) {
                        val stockResponse = response.getOrNull()

                        // Process valid stock data
                        stockResponse?.let {
                            val latestTime = it.timeSeries?.keys?.firstOrNull()
                            val latestData = it.timeSeries?.get(latestTime)

                            latestData?.let { data ->
                                val change = calculateChange(it.timeSeries)
                                fetchedStocks.add(
                                    StockData(
                                        name = it.metaData?.symbol ?: "Unknown",
                                        displayName = symbol,
                                        price = data.close?.toFloatOrNull() ?: 0f,
                                        change = change
                                    )
                                )
                            }
                        }
                    } else {
                        val errorBody = response?.exceptionOrNull()?.message ?: ""
                        Log.e("StockViewModel", "Error body for $symbol: $errorBody")

                        // Check for API limit exceeded message
                        if (errorBody.contains("https://www.alphavantage.co/premium/")) {
                            Toast.makeText(context, "API limit exceeded, showing mock data", Toast.LENGTH_LONG).show()
                            useMockData = true
                            break
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("StockViewModel", "Exception during stock fetching", e)
            }

            // If the API limit was exceeded or there was an error, use mock data
            if (useMockData || fetchedStocks.isEmpty()) {
                _stocks.value = mockStocks
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(context, "API limit exceeded, showing mock data", Toast.LENGTH_LONG).show()
                }
                Log.d("StockViewModel", "Showing mock data due to API limit or failure")
            } else {
                _stocks.value = fetchedStocks
                Log.d("StockViewModel", "Fetched real data: $fetchedStocks")
            }

            _isLoading.value = false
        }
    }

    // Helper function to calculate percentage change between the latest and previous data
    private fun calculateChange(timeSeries: Map<String, TimeSeriesData>): String {
        val values = timeSeries.values.take(2)
        if (values.size < 2) return "0.00%"
        val latest = values[0].close!!.toFloat()
        val previous = values[1].close!!.toFloat()
        val change = ((latest - previous) / previous) * 100
        return String.format("%.2f%%", change)
    }
}
