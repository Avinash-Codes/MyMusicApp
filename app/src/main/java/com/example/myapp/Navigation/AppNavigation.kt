package com.example.myapp.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.composelearning.AppUI.LoginScreen
import com.example.composelearning.AppUI.SignUpScreen
import com.example.myapp.appUI.DashBoardScreen
import com.example.myapp.appUI.ForgetPasswordScreen
import com.example.myapp.viewModel.AuthViewModel

@Composable
fun AppNav(navController: NavHostController, viewModel: AuthViewModel){
    NavHost(navController = navController, startDestination = "RegistrationScreen") {
            composable("RegistrationScreen") {
                SignUpScreen(viewModel,navController)
            }
            composable("LoginScreen"){
                LoginScreen(viewModel,navController)
            }
            composable("ForgetPasswordScreen") {
                ForgetPasswordScreen(viewModel,navController)
            }
        composable("DashBoardScreen"){
            DashBoardScreen(viewModel,navController)
        }

        }
    }


