package com.example.myapp.appUI.auth

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.example.myapp.R
import com.example.myapp.viewModel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn

@Composable
fun LoginScreen(viewModel: AuthViewModel, navController: NavHostController) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val isError by remember { mutableStateOf(false) }
    val errorMessage by remember { mutableStateOf("") }
    val isLoading by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }


    val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    val context = LocalContext.current

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            viewModel.handleGoogleSignInResult(task) // Handle the result in ViewModel
        }
    }




    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        //Welcome Screen Section
        Image(
            painter = painterResource(id = R.drawable.musicccc), contentDescription = "Login Image",
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))

        Text(text = "Welcome", fontSize = 28.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Login to your account")

        Spacer(modifier = Modifier.height(16.dp))

        //Email Section
        OutlinedTextField(
            value = email, onValueChange = {
                email = it
                //validate format
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

        //Password Section
        OutlinedTextField(value = password, onValueChange = {
            password = it
        }, label = { Text(text = "Password") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            isError = isError
        )
        if (isError){
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))


        //Button Section
        Button(onClick = {
            viewModel.loginUser(email, password)

        },
            enabled = !isLoading
        ) {
            if (isLoading){
                CircularProgressIndicator(color = Color.Red, modifier = Modifier.size(24.dp))
            }else{
                Text(text = "Login")

            }
        }
        val loginStatus by viewModel.loginStatus.observeAsState()
        viewModel.loginStatus.observe(LocalLifecycleOwner.current) { isSuccess ->
            if (loginStatus != null) {
                if (loginStatus == true) {
                    Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                    navController.navigate("DashBoardScreen")
            }  else {
                    // Handle different cases for incorrect email or password
                    when {
                        !email.isNullOrEmpty() && !password.isNullOrEmpty() -> {
                            Toast.makeText(context, "Incorrect email or password", Toast.LENGTH_SHORT)
                                .show()
                        }
                        email.isNullOrEmpty() -> {
                            Toast.makeText(context, "Email does not match", Toast.LENGTH_SHORT)
                                .show()
                        }
                        else -> {
                            Toast.makeText(
                                context,
                                "Account does not exist, please create an account",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        Text(text = "Forgot Password?", modifier = Modifier.clickable {
            navController.navigate("ForgetPasswordScreen")
        })

        Spacer(modifier = Modifier.height(32.dp))

        //Other SignIn Options Section
        Text(text = "or SignIn with")

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Image(painter = painterResource(id = R.drawable.instagram),
                contentDescription = "Login Image",
                modifier = Modifier
                    .size(50.dp)
                    .clickable {
                        //Instagram Login
                    }
            )
            Image(painter = painterResource(id = R.drawable.twitter),
                contentDescription = "Login Image",
                modifier = Modifier
                    .size(50.dp)
                    .clickable {
                        //Twitter Login
                    }
            )
            Image(painter = painterResource(id = R.drawable.search),
                contentDescription = "Login Image",
                modifier = Modifier
                    .size(50.dp)
                    .clickable {

                        viewModel.googleSignIn(googleSignInLauncher)


                    }
            )

        }
        viewModel.googleSignInStatus.observe(LocalLifecycleOwner.current) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(context, "Google Sign-In successful", Toast.LENGTH_SHORT).show()
                navController.navigate("DashboardScreen")
            } else {
                Toast.makeText(context, "Google Sign-In failed", Toast.LENGTH_SHORT).show()
            }
        }


    }

}

