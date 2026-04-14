package com.dungtran.codebase.ui.common

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage

@Composable
fun UserAvatarView(
    imageUrl: String?,
    size: Dp = 100.dp,
    borderWidth: Dp = 3.dp,
    borderColor: Color = Color(0xFF70C8C8),
    shape: Shape = CircleShape,
    contentDescription: String? = "User Avatar"
) {
    Box(
        modifier = Modifier
            .size(size) 
            .border(width = borderWidth, color = borderColor, shape = shape) // Vẽ viền tròn
            .clip(shape) 
    ) {
        SubcomposeAsyncImage(
            model = imageUrl,
            contentDescription = contentDescription,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            
            loading = {
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = borderColor)
                }
            },
            
            error = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                     Icon(imageVector = Icons.Default.Person, contentDescription = null)
                }
            }
        )
    }
}