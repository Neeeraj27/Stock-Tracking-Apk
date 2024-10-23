package com.example.stocktrackingapk.utils


import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class PrefManager @Inject constructor(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE)


    // Save login state (could be a token or a boolean flag)
    fun saveLoginState(isLoggedIn: Boolean) {
        sharedPreferences.edit().putBoolean("is_logged_in", isLoggedIn).apply()
    }

    // Check if the user is already logged in
    fun isUserLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("is_logged_in", false)
    }

    // Clear login state when user logs out
    fun clearLoginState() {
        sharedPreferences.edit().remove("is_logged_in").apply()
    }


}

