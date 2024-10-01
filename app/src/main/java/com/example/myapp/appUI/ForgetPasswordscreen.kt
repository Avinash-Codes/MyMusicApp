package com.example.myapp.appUI

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.example.myapp.viewModel.AuthViewModel

@Composable
fun ForgetPasswordScreen(viewModel: AuthViewModel, navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var emailExists by remember { mutableStateOf(false) }
    var showPasswordFields by remember { mutableStateOf(false) }
    var newPassword by remember { mutableStateOf("") }
    var reenterPassword by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var emailSent by remember { mutableStateOf(false) }
    var oldPassword by remember { mutableStateOf("") } // Added oldPassword

    val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // "Recover Your Password" Text
        Text(
            text = "Recover Your Password",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Email Section
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = !it.matches(emailPattern.toRegex())
            },
            label = { Text(text = "Email address") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = emailError
        )
        if (emailError) {
            Text(
                text = "Invalid email address",
                color = Color.Red,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))

        // Check Button
        Button(
            onClick = {
                if (email.matches(emailPattern.toRegex()) && !emailSent) {
                    viewModel.checkUserEmail(email)
                } else {
                    emailError = true
                }
            }
        ) {
            Text(text = "Check")
        }

        // Observe the email verification status
        viewModel.emailExists.observe(LocalLifecycleOwner.current) { exists ->
            if (exists) {
                showPasswordFields = true
                emailExists = true
                errorMessage = ""
                // Send password reset email if it hasn't been sent
                if (!emailSent) {
                    viewModel.sendPasswordResetEmail(email) { isSuccessful, errorMsg ->
                        if (isSuccessful) {
                            Toast.makeText(context, "Verification email sent", Toast.LENGTH_SHORT)
                                .show()
                            emailSent = true // Set to true after sending the email
                        } else {
                            Toast.makeText(context, "Error: $errorMsg", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                errorMessage = "Email not found"
                emailExists = false
                emailSent = false // Reset email sent status
            }
        }

        if (!emailExists && errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Show password fields only if the email exists
        if (showPasswordFields) {
            OutlinedTextField(
                value = newPassword,
                onValueChange = {
                    newPassword = it
                    passwordError = newPassword.length < 8
                },
                label = { Text(text = "New Password") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = reenterPassword,
                onValueChange = {
                    reenterPassword = it
                    passwordError = newPassword != reenterPassword
                },
                label = { Text(text = "Re-enter New Password") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                isError = passwordError
            )
            if (passwordError) {
                Text(
                    text = "Passwords do not match",
                    color = Color.Red,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            Button(
                onClick = {
                    if (newPassword == reenterPassword) {
                        viewModel.updateUserPassword(oldPassword,newPassword) { isSuccessful ->
                            if (isSuccessful) {
                                Toast.makeText(
                                    context,
                                    "Password updated successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                // Reset fields and flags after successful update
                                email = ""
                                newPassword = ""
                                reenterPassword = ""
                                emailExists = false
                                showPasswordFields = false
                                emailSent = false
                            } else {
                                Toast.makeText(
                                    context,
                                    "Error updating password",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } else {
                        Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                    }
                },
                enabled = !passwordError && newPassword.isNotEmpty() && reenterPassword.isNotEmpty()
            ) {
                Text(text = "Update Password")

            }
        }
    }
}


