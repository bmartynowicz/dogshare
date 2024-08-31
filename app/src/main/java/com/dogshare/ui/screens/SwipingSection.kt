package com.dogshare.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.dogshare.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipingSection() {
    // Load the image using Coil's rememberImagePainter
    val imagePainter = rememberAsyncImagePainter(R.drawable.dog_image)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray), // Optional background color
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = imagePainter,
            contentDescription = "Dog Image",
            modifier = Modifier.fillMaxSize()
        )
        Text(
            text = "Swipe right if you like the dog.",
            style = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }
}
