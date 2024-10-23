package com.example.stocktrackingapk.utils



import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable

import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.stocktrackingapk.SplashScreen
import com.example.stocktrackingapk.ui.StockScreen.ChartScreen
import com.example.stocktrackingapk.ui.StockScreen.StockScreen
import com.example.stocktrackingapk.ui.StockScreen.StockViewModel
import com.example.stocktrackingapk.ui.loginSignup.AuthViewModel
import com.example.stocktrackingapk.ui.loginSignup.LoginScreen
import com.example.stocktrackingapk.ui.loginSignup.SignupScreen

@RequiresApi(Build.VERSION_CODES.S)
@SuppressLint("RememberReturnType", "SuspiciousIndentation")
@Composable
fun AppNavigation(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    stockViewModel: StockViewModel
)
{

    NavHost(navController, startDestination = Screen.SplashScreen.route) {
        composable(Screen.LoginScreen.route)
        {
            LoginScreen(navController = navController,authViewModel)
        }
        composable(Screen.SignupScreen.route)
        {
            SignupScreen(navController = navController,authViewModel)
        }
        composable(Screen.StockScreen.route)
        {
            StockScreen(navController,stockViewModel,authViewModel)
        }
        composable(Screen.SplashScreen.route)
        {
            SplashScreen(navController,authViewModel)
        }
        composable(
            route = "${Screen.ChartScreen.route}/{stockName}",
            arguments = listOf(navArgument("stockName") { type = NavType.StringType })
        ) { backStackEntry ->
            val stockName = backStackEntry.arguments?.getString("stockName") ?: ""
            ChartScreen(navController, stockViewModel, stockName)
        }
    }
}