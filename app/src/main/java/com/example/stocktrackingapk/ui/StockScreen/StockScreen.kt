package com.example.stocktrackingapk.ui.StockScreen

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.stocktrackingapk.R
import com.example.stocktrackingapk.ui.loginSignup.AuthViewModel
import com.example.stocktrackingapk.utils.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun StockScreen(navController: NavHostController, stockViewModel: StockViewModel, authViewModel: AuthViewModel) {
    // Local state to track which button is selected
    var selectedOption by remember { mutableStateOf("Explore") }
    var backPressedOnce by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Collect stocks data from the ViewModel
    val stocks by stockViewModel.stocks.collectAsState()
    val isLoading by stockViewModel.isLoading.collectAsState()

    // Loading animation
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
    val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)

    // Handle back press to show a toast for double-back exit
    BackHandler {
        if (backPressedOnce) {
            (context as Activity).finish() // Exit app
        } else {
            Toast.makeText(context, "Press back again to exit", Toast.LENGTH_SHORT).show()
            backPressedOnce = true
        }
    }

    // Reset back press state after 2 seconds
    LaunchedEffect(backPressedOnce) {
        if (backPressedOnce) {
            delay(2000L)
            backPressedOnce = false
        }
    }

    // Fetch stock data on initial launch
    LaunchedEffect(Unit) {
        try {
            stockViewModel.fetchStocks(
                listOf(
                    "AAPL", "GOOGL", "MSFT", "AMZN", "META", "TSLA",
                    "ORCL", "V", "UNH", "JPM", "AVGO", "MA"
                ), context
            )
        } catch (e: Exception) {
            Toast.makeText(context, "Error fetching data: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Header row with logout button
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp)
        ) {
            Text(
                text = "US Market Overview",
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                modifier = Modifier.padding(start = 6.dp)
            )

            IconButton(onClick = { stockViewModel._showLogoutDialogBox.value = true }) {
                Icon(Icons.Filled.ExitToApp, contentDescription = null)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))


        // Explore buttons row
        ExploreButtons(
            selectedOption = selectedOption,
            onOptionSelected = { selectedOption = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Conditional UI based on selected option
        when (selectedOption) {
            "Explore" -> {
                if (isLoading) {
                    // Shows loading animation while fetching data
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        LottieAnimation(
                            composition = composition,
                            progress = { progress },
                            modifier = Modifier.size(140.dp)
                        )
                    }
                } else {
                    // Display stock list
                    LazyColumn {
                        items(stocks.chunked(2)) { stockPair ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp)
                            ) {
                                StockCard(
                                    stock = stockPair[0],
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(150.dp),
                                    imageUrl = stockImages[stocks.indexOf(stockPair[0])],
                                    onClick = {
                                        navController.navigate("${Screen.ChartScreen.route}/${stockPair[0].name}")
                                    }
                                )

                                if (stockPair.size > 1) {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    StockCard(
                                        stock = stockPair[1],
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(150.dp),
                                        imageUrl = stockImages[stocks.indexOf(stockPair[1])],
                                        onClick = {
                                            navController.navigate("${Screen.ChartScreen.route}/${stockPair[1].name}")
                                        }
                                    )
                                } else {
                                    // Spacer for single-item rows
                                    Spacer(modifier = Modifier.weight(1f).height(40.dp))
                                }
                            }
                        }
                    }
                }
            }
            "Holdings" -> {
                EmptyMessage("You have no holdings", animationResId = R.raw.noholdings)
            }
            "Watchlist" -> {
                EmptyMessage("You have no watchlist for now", animationResId = R.raw.nowatchlist)
            }
        }

        // Logout dialog

    }
    if (stockViewModel.showLogoutDialogBox.value) {
        // Dialog box that appears on top of everything
        Dialog(
            onDismissRequest = { stockViewModel._showLogoutDialogBox.value = false }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF61A4F1), // top blue
                                Color(0xFF56C2D6)  // bottom cyan
                            )
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    Text(
                        text = "Are you sure? You want to logout",
                        style = MaterialTheme.typography.h6,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                stockViewModel._showLogoutDialogBox.value = false
                                authViewModel.logout(navController)
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "YES", color = Color.White)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            onClick = { stockViewModel._showLogoutDialogBox.value = false },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFB1E1FF)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "NO", color = Color.Black)
                        }
                    }
                }
            }
        }
    }

}


@Composable
fun ExploreButtons(selectedOption: String, onOptionSelected: (String) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        listOf("Explore", "Holdings", "Watchlist").forEach { option ->
            Card(
                shape = RoundedCornerShape(50),
                backgroundColor = if (selectedOption == option) Color.Transparent else Color.White,
                border = if (selectedOption == option) BorderStroke(2.dp, Color.Gray) else null,
                modifier = Modifier
                    .height(40.dp)
                    .clip(RoundedCornerShape(50))
                    .padding(horizontal = 4.dp)
                    .clickable { onOptionSelected(option) }
            ) {
                Box(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = option,
                        style = MaterialTheme.typography.body1,
                        color = if (selectedOption == option) Color.Black else Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun StockCard(stock: StockData, modifier: Modifier = Modifier, imageUrl: String, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            // Image at the top of the card
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Stock display name
            Text(
                text = stock.displayName,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Stock price
            Text(
                text = "${stock.price}",
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            // Stock price change
            Text(
                text = stock.change,
                color = if (stock.change.contains("-")) Color.Red else Color.Green,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.align(Alignment.CenterHorizontally) // Center the change percentage
            )
        }
    }
}


// Mock images for stock cards
val stockImages = listOf(
    "https://logo.clearbit.com/apple.com",
    "https://logo.clearbit.com/google.com",
    "https://logo.clearbit.com/microsoft.com",
    "https://logo.clearbit.com/amazon.com",
    "https://logo.clearbit.com/meta.com",
    "https://logo.clearbit.com/tesla.com",
    "https://logo.clearbit.com/oracle.com",
    "https://logo.clearbit.com/visa.com",
    "https://logo.clearbit.com/uhc.com",
    "https://logo.clearbit.com/jpmorganchase.com",
    "https://logo.clearbit.com/broadcom.com",
    "https://logo.clearbit.com/mastercard.com"
)

// Data classes for stock and index data
data class StockData(val name: String, val displayName: String, val price: Float, val change: String)

@Composable
fun EmptyMessage(message: String, animationResId: Int) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(animationResId))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever // Loops the animation infinitely
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Lottie animation for "no data"
        LottieAnimation(
            composition,
            progress,
            modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Display the message below the animation
        Text(
            text = message,
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Center
        )
    }
}
