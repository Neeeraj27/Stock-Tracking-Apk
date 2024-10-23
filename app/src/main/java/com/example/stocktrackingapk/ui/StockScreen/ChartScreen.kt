package com.example.stocktrackingapk.ui.StockScreen

import android.webkit.WebView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.example.stocktrackingapk.utils.vibrate

@Composable
fun ChartScreen(navController: NavHostController, stockViewModel: StockViewModel, stockName: String) {
    var selectedInterval by remember { mutableStateOf("1D") } // Default to 1 Day interval
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize()) {
        // Displaying the stock name at the top of the screen
        Text(
            text = stockName,
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = 30.dp, start = 6.dp, bottom = 6.dp)
        )

        // WebView to display TradingView chart with selected interval
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        loadWithOverviewMode = true
                        useWideViewPort = true
                    }
                    // Loading chart content when WebView is first created
                    loadChartContent(this, stockName, selectedInterval)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            update = { webView ->
                // Reloads chart content when interval changes
                loadChartContent(webView, stockName, selectedInterval)
            }
        )

        // Interval selection buttons at the bottom
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        ) {
            val intervals = listOf("1D", "1W", "1M", "1Y", "5Y") // Available intervals
            intervals.forEach { interval ->
                TextButton(
                    onClick = {
                        selectedInterval = interval // Changes interval when clicked
                        vibrate(context) // Provide haptic feedback on click
                    },
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = if (selectedInterval == interval) Color.Gray else Color.Transparent,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .background(Color.Transparent)
                ) {
                    Text(
                        text = interval,
                        color = if (selectedInterval == interval) Color.Black else Color.Gray,
                        style = MaterialTheme.typography.body1
                    )
                }
            }
        }
    }
}

// Helper function to load chart content into the WebView based on stock name and selected interval
private fun loadChartContent(webView: WebView, stockName: String, interval: String) {
    val widgetUrl = """
        <html>
        <body style="margin:0;padding:0;">
        <div class="tradingview-widget-container">
          <div id="tradingview_12345"></div>
          <script type="text/javascript" src="https://s3.tradingview.com/tv.js"></script>
          <script type="text/javascript">
          new TradingView.widget({
            "container_id": "tradingview_12345",
            "width": "100%",
            "height": "1800",
            "symbol": "$stockName",
            "interval": "${mapIntervalToTradingView(interval)}",
            "timezone": "Etc/UTC",
            "theme": "light",
            "style": "1",
            "locale": "en",
            "toolbar_bg": "#f1f3f6",
            "enable_publishing": false,
            "hide_top_toolbar": true,
            "save_image": false
          });
          </script>
        </div>
        </body>
        </html>
    """.trimIndent()

    // Loads the HTML content with the widget URL
    webView.loadDataWithBaseURL(null, widgetUrl, "text/html", "utf-8", null)
}

// Helper function to map the selected interval to TradingView widget format
private fun mapIntervalToTradingView(interval: String): String {
    return when (interval) {
        "1D" -> "D"    // 1 Day
        "1W" -> "W"    // 1 Week
        "1M" -> "M"    // 1 Month
        "1Y" -> "12M"  // 1 Year
        "5Y" -> "60M"  // 5 Years
        else -> "D"    // Default to 1 Day
    }
}
