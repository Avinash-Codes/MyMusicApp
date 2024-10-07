package com.example.myapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.myapp.Navigation.AppNav

import com.example.myapp.viewModel.AuthViewModel
import com.example.myapp.viewModel.ProfileUpdateViewModel
import com.google.firebase.Firebase
import com.google.firebase.initialize

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Firebase.initialize(this)

        setContent {
            val navController = rememberNavController()
            val viewModel: AuthViewModel = viewModel()
            val authviewModel: ProfileUpdateViewModel = viewModel()
            AppNav(navController, viewModel, authviewModel, contentResolver )

        }
    }
}

