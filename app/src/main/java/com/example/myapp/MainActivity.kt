package com.example.myapp

import SearchViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.myapp.Navigation.AppNav

import com.example.myapp.viewModel.AuthViewModel
import com.google.firebase.Firebase
import com.google.firebase.initialize

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Firebase.initialize(this)

        setContent {
            val navController = rememberNavController()
            val viewModel: AuthViewModel = viewModel()
            val searchViewModel: SearchViewModel = viewModel()
            AppNav(navController, viewModel, searchViewModel)

        }
    }
}

