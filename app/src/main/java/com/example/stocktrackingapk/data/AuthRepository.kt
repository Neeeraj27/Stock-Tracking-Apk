package com.example.stocktrackingapk.data

import android.util.Log
import javax.inject.Inject

class AuthRepository @Inject constructor(private val apiService: ApiService) {

    // Log in a user by checking email and password
    suspend fun loginUser(email: String, password: String): Result<SignupResponse?> {
        return try {
            val response = apiService.login()

            if (response.isSuccessful) {
                val users = response.body()

                users?.let {
                    // Checking if the email exists and if the password matches
                    val userByEmail = users.firstOrNull { it.email == email }
                    return when {
                        userByEmail == null -> Result.failure(Exception("User not found"))
                        userByEmail.password != password -> Result.failure(Exception("Incorrect password"))
                        else -> Result.success(userByEmail)
                    }
                }
                Result.failure(Exception("No users available"))
            } else {
                Result.failure(Exception("Failed to retrieve users"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Sign up a new user
    suspend fun signupUser(id: String, email: String, password: String): Result<SignupResponse> {
        return try {
            val response = apiService.signup(SignupRequest(id, email, password))

            if (response.isSuccessful) {
                Log.d("API Response", "Sign-up successful: ${response.body()}")
                Result.success(response.body()!!)
            } else {
                Log.e("API Error", "Error response: ${response.errorBody()?.string()}")
                Result.failure(Exception("Signup failed"))
            }
        } catch (e: Exception) {
            Log.e("API Error", "Exception during signup: ${e.message}")
            Result.failure(e)
        }
    }
}
