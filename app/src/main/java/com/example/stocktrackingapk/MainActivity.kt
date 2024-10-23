package com.example.stocktrackingapk

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.navigation.compose.rememberNavController
import com.example.stocktrackingapk.ui.StockScreen.StockViewModel
import com.example.stocktrackingapk.ui.loginSignup.AuthViewModel
import com.example.stocktrackingapk.ui.theme.StockTrackingAPKTheme
import com.example.stocktrackingapk.utils.AppNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    private val stockViewModel: StockViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StockTrackingAPKTheme {
                val navController = rememberNavController()
                AppNavigation(navController = navController,authViewModel,stockViewModel)
            }
        }
    }
}

