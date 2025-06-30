package com.example.podhub.utils

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ShimmerPodcastRow() {
    // Animation for shimmer
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f)
    )

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f, // Large enough for shimmer to move across
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "shimmerAnim"
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim, translateAnim),
        end = Offset(translateAnim + 200f, translateAnim + 200f)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        // Circular shimmer for image
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(brush, shape = CircleShape)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            // Rectangle shimmer for title
            Box(
                modifier = Modifier
                    .height(18.dp)
                    .width(120.dp)
                    .background(brush, shape = MaterialTheme.shapes.small)
            )
            Spacer(modifier = Modifier.height(6.dp))
            // Rectangle shimmer for subtitle
            Box(
                modifier = Modifier
                    .height(14.dp)
                    .width(80.dp)
                    .background(brush, shape = MaterialTheme.shapes.small)
            )
        }
    }
}