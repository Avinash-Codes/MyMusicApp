package com.example.myapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.myapp.Navigation.AppNav

import com.example.myapp.appUI.ForgetPasswordScreen

import com.example.myapp.viewModel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()
            val viewModel: AuthViewModel = viewModel()
            AppNav(navController, viewModel)
        }
    }
}

