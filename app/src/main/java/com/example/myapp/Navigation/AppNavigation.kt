package com.example.myapp.Navigation

import SearchViewModel
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myapp.appUI.auth.LoginScreen
import com.example.myapp.appUI.auth.SignUpScreen
import com.example.myapp.appUI.auth.ForgetPasswordScreen
import com.example.myapp.ui.AnimatedSplashScreen
import com.example.myapp.ui.MusicDashboard
import com.example.myapp.ui.SearchScreen
import com.example.myapp.viewModel.AuthViewModel

@Composable
fun AppNav(navController: NavHostController, viewModel: AuthViewModel, searchViewModel: SearchViewModel){
    NavHost(navController = navController, startDestination = "SplashScreen") {
            composable("SplashScreen"){
                AnimatedSplashScreen(navController)
            }
            composable("SignUp") {
                SignUpScreen(viewModel,navController)
            }
            composable("LoginScreen"){
                LoginScreen(viewModel,navController)
            }
            composable("ForgetPasswordScreen") {
                ForgetPasswordScreen(viewModel,navController)
            }
            composable("DashBoardScreen"){
                MusicDashboard(navController)
            }
            composable("SearchScreen"){
                SearchScreen(searchViewModel, navController)
            }




        }
    }


