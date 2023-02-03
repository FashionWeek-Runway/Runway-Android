package com.cmc12th.runway.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmc12th.runway.R

@Composable
fun SplashScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .fillMaxHeight(0.7f),
            painter = painterResource(id = R.drawable.img_splash_background),
            contentDescription = "SPLAH_BACKGROUND",
            contentScale = ContentScale.Crop)
        Image(
            modifier = Modifier
                .align(Alignment.Center)
                .size(94.dp, 100.dp),
            painter = painterResource(id = R.drawable.img_splash_logo),
            contentDescription = "SPLAH_LOGO")

        Text(modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 20.dp),
            text = "RUNWAY",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen()
}

