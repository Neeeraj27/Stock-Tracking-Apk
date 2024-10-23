package com.example.stocktrackingapk.ui.loginSignup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.stocktrackingapk.utils.Screen

@Composable
fun SignupScreen(navController: NavHostController, viewModel: AuthViewModel) {
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Checks if the form is valid: all fields must be filled, and passwords must match
    val isFormValid = remember(viewModel.email, viewModel.password, viewModel.retypePassword) {
        viewModel.email.isNotEmpty() &&
                viewModel.password.isNotEmpty() &&
                viewModel.retypePassword.isNotEmpty() &&
                viewModel.password == viewModel.retypePassword
    }

    // Reset form fields when screen is first loaded
    LaunchedEffect(Unit) {
        viewModel.errorMessage = ""
        viewModel.email = ""
        viewModel.password = ""
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF61A4F1),
                        Color(0xFF56C2D6)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(16.dp)
                .background(
                    Color.White,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Create Your Account",
                style = MaterialTheme.typography.h5,
                color = Color(0xFF4CAF50),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Email Input Field
            TextField(
                value = viewModel.email,
                onValueChange = { viewModel.email = it },
                placeholder = { Text("Email or Username") },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFFF5F5F5),
                        shape = RoundedCornerShape(50.dp)
                    ),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            TextField(
                value = viewModel.password,
                onValueChange = { viewModel.password = it },
                placeholder = { Text("Password") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = if (passwordVisible) "Hide password" else "Show password")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFFF5F5F5),
                        shape = RoundedCornerShape(50.dp)
                    ),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            TextField(
                value = viewModel.retypePassword,
                onValueChange = { viewModel.retypePassword = it },
                placeholder = { Text("Confirm Password") },
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (confirmPasswordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(imageVector = image, contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFFF5F5F5),
                        shape = RoundedCornerShape(50.dp)
                    ),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            // Show error message when passwords do not match
            if (viewModel.password != viewModel.retypePassword && viewModel.retypePassword.isNotEmpty()) {
                Text(
                    text = "Confirm Password did not match",
                    color = Color.Red,
                    modifier = Modifier.padding(8.dp)
                )
            }

            // Shows any additional error messages (e.g., signup errors)
            if (viewModel.errorMessage.isNotEmpty()) {
                Text(text = viewModel.errorMessage, color = Color.Red, modifier = Modifier.padding(8.dp))
            }

            // Sign Up Button (enabled only if form is valid)
            Button(
                onClick = {
                    try {
                        if (isFormValid) {
                            viewModel.signup(navController, context)
                        }
                    } catch (e: Exception) {
                        viewModel.errorMessage = "An error occurred: ${e.message}"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (isFormValid) Color(0xFF1E88E5) else Color.Gray, // Button color changes based on validity
                        shape = RoundedCornerShape(50.dp)
                    ),
                enabled = isFormValid, // Disable button if form is invalid
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (isFormValid) Color(0xFF1E88E5) else Color.Gray
                )
            ) {
                Text(
                    text = "SIGN UP",
                    color = Color.White,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            // TextButton for navigating to LoginScreen
            TextButton(onClick = {
                navController.navigate(Screen.LoginScreen.route)
                viewModel.clearData() // Clear form data after navigating
            }) {
                Text("Already Registered? Please Login", color = Color(0xFF1E88E5), fontSize = 13.sp)
            }
        }
    }
}
