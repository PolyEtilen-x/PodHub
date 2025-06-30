package com.example.podhub.ui.feature.podcast

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.podhub.R
import com.example.podhub.models.PodcastResponseData
import com.example.podhub.data.PodcastResponse
import com.example.podhub.ui.components.PodcastItem
import com.example.podhub.ui.navigation.Routes
import com.example.podhub.utils.PodcastItemShimmer
import com.example.podhub.utils.createShimmerBrush

import com.example.podhub.viewmodels.PodcastViewModel

@Composable
fun PodcastCategoryScreen(navController: NavHostController,podCastViewModel: PodcastViewModel) {
    var searchQuery by remember { mutableStateOf("") }


    val podcasts by podCastViewModel.podcasts.collectAsState()
    val isLoading by podCastViewModel.isLoading.collectAsState()
    val chunkedPodcasts = podcasts.chunked(2)



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAF6F5))
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.Fit
        )

        Text(
            text = "Category of Podcast",
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFFFFC533)
        )

        Divider(color = Color(0xFFFFC533), thickness = 2.dp)

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFFC107),
                unfocusedBorderColor = Color(0xFFFFC107),
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (isLoading) {
                // Show shimmer loading items
                items(6) { // Show 6 shimmer rows (12 items total)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        PodcastItemShimmer(modifier = Modifier.weight(1f))
                        PodcastItemShimmer(modifier = Modifier.weight(1f))
                    }
                }
            } else {
                // Show actual podcast items
                items(chunkedPodcasts) { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        row.forEach { podcast ->
                            PodcastItem(
                                podcast = podcast,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        if (row.size < 2) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        Button(
            onClick = {
                navController.navigate(Routes.HOME)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFC533),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("START", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }
    }
}