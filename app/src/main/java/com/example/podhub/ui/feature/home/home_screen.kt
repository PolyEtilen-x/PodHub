package com.example.podhub.ui.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.podhub.components.HomeHeader
import com.example.podhub.models.UserModel
import com.example.podhub.viewmodels.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(
    userViewModel: UserViewModel = viewModel(),
    navController: NavHostController
) {
    val user = userViewModel.currentUser

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // HEADER
        HomeHeader(
            userName = user?.displayName ?: "Guest",
            userAvatarUrl = user?.photoUrl ?: ""
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "UID: ${user?.uid ?: "Unknown"}")
            Text(text = "Email: ${user?.email ?: "N/A"}")
        }
    }
}

