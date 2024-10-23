package com.example.stocktrackingapk.components

import android.util.Log
import com.example.stocktrackingapk.ui.StockScreen.StockData
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject

class WebSocketManager {
    private var socket: Socket? = null

    // Connect to the WebSocket and handle incoming stock updates
    fun connect(
        url: String,
        onStockUpdate: (StockData) -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            // Establish connection to the provided WebSocket URL
            socket = IO.socket(url)
            socket?.on(Socket.EVENT_CONNECT) {
                Log.d("WebSocketManager", "Connected to WebSocket")
            }?.on("stockUpdate") { args ->
                if (args.isNotEmpty() && args[0] is JSONObject) {
                    val jsonObject = args[0] as JSONObject
                    // Parse the received JSON object to a StockData object
                    val stockData = parseStockData(jsonObject)
                    onStockUpdate(stockData)
                }
            }?.on(Socket.EVENT_DISCONNECT) {
                Log.d("WebSocketManager", "Disconnected from WebSocket")
            }?.on(Socket.EVENT_CONNECT_ERROR) {
                Log.e("WebSocketManager", "Connection Error")
                onError("Connection Error")
            }
            socket?.connect()
        } catch (e: Exception) {
            Log.e("WebSocketManager", "Exception: ${e.message}")
            onError(e.message ?: "Unknown error")
        }
    }

    // Disconnect and remove event listeners
    fun disconnect() {
        socket?.disconnect()
        socket?.off()
    }

    // Parse the incoming JSON data to a StockData object
    private fun parseStockData(jsonObject: JSONObject): StockData {
        return StockData(
            name = jsonObject.getString("stockName"), // Ensure the key matches the JSON field
            displayName = jsonObject.optString("displayName", "N/A"), // Optional field example
            price = jsonObject.getDouble("price").toFloat(),
            change = jsonObject.optString("change", "0.0%") // Handle percentage change as a string
        )
    }
}
