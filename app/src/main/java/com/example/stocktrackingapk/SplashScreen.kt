package com.example.stocktrackingapk

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.stocktrackingapk.ui.loginSignup.AuthViewModel
import com.example.stocktrackingapk.utils.Screen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController, viewModel: AuthViewModel) {
    // Lottie animation composition
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.splashscreen)) // Replace with your animation file
    val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)

    // Splash screen background (assuming a gradient for the background)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF61A4F1), // top blue
                        Color(0xFF56C2D6)  // bottom cyan
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Display the Lottie animation
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.size(200.dp) // Adjust size as needed
        )
    }

    // Delay of 2 seconds before navigating
    LaunchedEffect(Unit) {
        delay(2000L) // 2-second delay

        // Check if the user is logged in
        if (viewModel.isLoggedIn) {
            navController.navigate(Screen.StockScreen.route) {
                popUpTo(Screen.SplashScreen.route) { inclusive = true }
            }
        } else {
            navController.navigate(Screen.LoginScreen.route) {
                popUpTo(Screen.SplashScreen.route) { inclusive = true }
            }
        }
    }
}
