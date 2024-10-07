package com.example.myapp.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.traceEventStart
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.myapp.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun AnimatedSplashScreen(navController: NavHostController){
    var startAnimation by remember {
        mutableStateOf(false)
    }
    val alphaAnim = animateFloatAsState(targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 3000
        )
    )
    val auth = FirebaseAuth.getInstance()

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(4000)

        if (auth.currentUser!=null){
            navController.popBackStack()
            navController.navigate("DashBoardScreen")
        }
        else{
            navController.popBackStack()
            navController.navigate("SignUp")
        }
    }
    Splash(alpha = alphaAnim.value)

}
@Composable
fun Splash(alpha:Float){
    Box (
        modifier = Modifier
            .background(Color.Cyan)
            .fillMaxSize()
            .size(120.dp),
        contentAlignment = Alignment.Center

    ){
        Icon(painter = painterResource(id = R.drawable.headphone), contentDescription = "Icon", modifier = Modifier
            .alpha(alpha = alpha))
    }
}
@Composable
@Preview
fun SplashScreenPreview(){
    Splash(alpha = 1f)

}
@Composable
@Preview
fun SplashScreenDarkPreview(){
    Splash(alpha = 1f)

}