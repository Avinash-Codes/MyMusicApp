package com.example.myapp.appUI.auth

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import android.widget.Toast
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.example.myapp.R
import com.example.myapp.viewModel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn


@Composable
fun SignUpScreen(viewModel: AuthViewModel, navController: NavHostController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var accountExistsError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }


    val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    val context = LocalContext.current

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            viewModel.handleGoogleSignInResult(task) // Call your ViewModel to handle the result
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
        Text(
            text = "Hello User",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Sign up to your account")
        Spacer(modifier = Modifier.height(16.dp))


        //Text Fields code
        //Name Section
        OutlinedTextField(value = name, onValueChange = {
            name = it
        }, label = { Text(text = "Name") },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(10.dp))

        //Email Section
        OutlinedTextField(
            value = email, onValueChange = {
                email = it
                //validate format
                emailError = !it.matches(emailPattern.toRegex())
            },
            label = { Text(text = "Email address") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = emailError || accountExistsError
        )
        if (emailError) {
            Text(
                text = "Invalid email address",
                color = Color.Red,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        if (accountExistsError) {
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))

        //Password Section
        OutlinedTextField(value = password, onValueChange = {
            password = it
            passwordError = it.length < 8 && password != confirmPassword
        }, label = { Text(text = "Create New Password") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(10.dp))


        //Confirm Password Section
        OutlinedTextField(value = confirmPassword, onValueChange = {
            confirmPassword = it
            passwordError = password != confirmPassword
        }, label = { Text(text = "Re-enter Password") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            isError = passwordError
        )
        if (passwordError) {
            Text(
                text = "Password do not match",
                color = Color.Red,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))


        //BUTTON Section
        Button(
            onClick = {
                if(email.isEmpty()||password.isEmpty()||name.isEmpty()||confirmPassword.isEmpty()) {
                    Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show()

                }else {
                    if (password == confirmPassword) {
                        viewModel.registerUser(name, email, password)
                    } else {
                        Toast.makeText(context, "Password do not match", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            enabled = !emailError && !passwordError
        ) {
            Text(text = "Sign Up")
        }
        viewModel.registrationStatus.observe(LocalLifecycleOwner.current) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(context, "Registration successful", Toast.LENGTH_SHORT).show()
                navController.navigate("DashBoardScreen")
            } else {
                Toast.makeText(context, "Registration failed", Toast.LENGTH_SHORT).show()
            }
        }
        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "Already have an account?", modifier = Modifier.clickable {
            navController.navigate("LoginScreen")

        })

        Spacer(modifier = Modifier.height(20.dp))

        //Another SignIn option Section
        Text(text = "or SignIn with", fontWeight = FontWeight.Bold)

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
                        //Google Login
                        viewModel.googleSignIn(googleSignInLauncher)
                    }
            )
        }
        viewModel.googleSignInStatus.observe(LocalLifecycleOwner.current) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(context, "Google Sign-In successful", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Google Sign-In failed", Toast.LENGTH_SHORT).show()
            }
        }

    }
}


