package com.example.myapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import com.example.composelearning.AppUI.LoginScreen
import com.example.composelearning.AppUI.SignUpScreen
import com.example.myapp.appUI.ForgetPasswordScreen

import com.example.myapp.viewModel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            ForgetPasswordScreen(viewModel = AuthViewModel(application))
        }
    }
}

