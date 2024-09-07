package com.dogshare.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dogshare.R

@Composable
fun Greeting(username: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.dog_image),
            contentDescription = "Background image of a dog",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalArrangement = Arrangement.Bottom
        ) {
            Button(
                onClick = { /* TODO: Define the action */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp), // Adjust this value or use a percentage of screen height if needed
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1F223C))
            ) {
                Text(
                    "Greetings",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
    }
}