package com.example.podhub.ui.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.podhub.models.UserModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(navHostController: NavHostController) {
    val currentUser = FirebaseAuth.getInstance().currentUser

    val user = UserModel(
        uid = currentUser?.uid ?: "",
        displayName = currentUser?.displayName,
        email = currentUser?.email,
        photoUrl = currentUser?.photoUrl?.toString()
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF6F6F6)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Ảnh đại diện
            if (user.photoUrl != null) {
                Image(
                    painter = rememberAsyncImagePainter(user.photoUrl),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(120.dp)
                        .padding(top = 24.dp)
                        .aspectRatio(1f)
                        .align(Alignment.CenterHorizontally),
                    contentScale = ContentScale.Crop
                )
            }

            Text(
                text = user.displayName ?: "No Name",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            // Email
            Text(
                text = user.email ?: "No Email",
                fontSize = 18.sp
            )

            // UID
            Text(
                text = "UID: ${user.uid}",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}
