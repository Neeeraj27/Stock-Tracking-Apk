package com.example.stocktrackingapk.utils

sealed class Screen(val route: String) {
    object LoginScreen : Screen("LoginScreen")
    object SignupScreen : Screen("SignupScreen")
    object StockScreen: Screen("StockScreen")
    object ChartScreen: Screen("ChartScreen")
    object SplashScreen: Screen("SplashScreen")

}
