package com.example.stocktrackingapk.ui.loginSignup

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.stocktrackingapk.data.AuthRepository
import com.example.stocktrackingapk.utils.PrefManager
import com.example.stocktrackingapk.utils.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val prefManager: PrefManager
) : ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var retypePassword by mutableStateOf("")
    var id by mutableStateOf(0)
    var isLoggedIn by mutableStateOf(prefManager.isUserLoggedIn())
    var errorMessage by mutableStateOf("")

    // Clears the form fields after login or signup
    fun clearData() {
        email = ""
        password = ""
        retypePassword = ""
    }

    // Handles user login, saves login state, and navigates to the StockScreen
    fun login(navController: NavController, context: Context) {
        viewModelScope.launch {
            try {
                val result = authRepository.loginUser(email, password)
                if (result.isSuccess) {
                    result.getOrNull()?.let {
                        isLoggedIn = true
                        prefManager.saveLoginState(true)  // Saving login state

                        // Navigating to StockScreen and clearing the back stack
                        navController.navigate(Screen.StockScreen.route) {
                            popUpTo(Screen.LoginScreen.route) { inclusive = true }
                        }
                    }
                } else {
                    errorMessage = result.exceptionOrNull()?.message ?: "Login failed"
                }
            } catch (e: Exception) {
                errorMessage = "An error occurred during login: ${e.message}"
            }
        }
    }

    // Logs out the user by clearing login state and navigating to the LoginScreen
    fun logout(navController: NavController) {
        isLoggedIn = false
        prefManager.clearLoginState()  // Clears login state

        // Navigating to LoginScreen and clearing the back stack
        navController.navigate(Screen.LoginScreen.route) {
            popUpTo(Screen.StockScreen.route) { inclusive = true }
        }
    }

    // Handles user signup, checks password match, and navigates to LoginScreen on success
    fun signup(navController: NavHostController, context: Context) {
        viewModelScope.launch {
            try {
                if (password == retypePassword) {
                    id += 1  // Increment the id for the new user
                    val result = authRepository.signupUser(id.toString(), email, password)
                    if (result.isSuccess) {
                        result.getOrNull()?.let {
                            Toast.makeText(context, "Signup successful!", Toast.LENGTH_SHORT).show()
                            navController.navigate(Screen.LoginScreen.route)

                            clearData()  // Clears form fields
                        }
                    } else {
                        errorMessage = result.exceptionOrNull()?.message ?: "Signup failed"
                    }
                } else {
                    errorMessage = "Passwords do not match"
                }
            } catch (e: Exception) {
                errorMessage = "An error occurred during signup: ${e.message}"
            }
        }
    }
}
