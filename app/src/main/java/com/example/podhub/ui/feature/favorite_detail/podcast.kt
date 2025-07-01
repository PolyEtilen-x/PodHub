package com.example.podhub.ui.feature.podcast

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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.podhub.R
import com.example.podhub.models.PodcastResponseData
import com.example.podhub.storage.SelectedPodcastStore
import com.example.podhub.ui.components.PodcastItem
import com.example.podhub.ui.navigation.Routes
import com.example.podhub.utils.PodcastItemShimmer
import com.example.podhub.viewmodels.PodcastViewModel
import kotlinx.coroutines.launch
import android.util.Log
import com.example.podhub.viewmodels.FavouriteViewModel

@Composable
fun PodcastCategoryScreen(navController: NavHostController, podCastViewModel: PodcastViewModel,favouriteViewModel: FavouriteViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    val podcasts by podCastViewModel.podcasts.collectAsState()
    val isLoading by podCastViewModel.isLoading.collectAsState()
    val selectedPodcasts = remember { mutableStateListOf<String>() }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val filteredPodcasts = podcasts.filter {
        it.trackName?.contains(searchQuery, ignoreCase = true) == true
    }


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
            text = "Podcast yêu thích",
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFFFFC533)
        )

        Divider(color = Color(0xFFFFC533), thickness = 2.dp)

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Tìm kiếm") },
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
            modifier = Modifier
                .weight(1f)
                .padding(top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (isLoading) {
                items(6) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        PodcastItemShimmer(modifier = Modifier.weight(1f))
                        PodcastItemShimmer(modifier = Modifier.weight(1f))
                    }
                }
            } else {
                val chunkedPodcasts = filteredPodcasts.chunked(2)

                items(chunkedPodcasts) { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        row.forEach { podcast ->
                            val isSelected = selectedPodcasts.contains(podcast.trackName)

                            PodcastItem(
                                podcast = podcast,
                                isSelected = isSelected,
                                onClick = {
                                    if (!isSelected) {
                                        selectedPodcasts.add(podcast.trackName ?: "")
                                    }
                                },
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


        if (selectedPodcasts.isNotEmpty()) {
            Text(
                text = "Bạn đã chọn ${selectedPodcasts.size} podcast",
                fontSize = 25.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFFFC533),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 8.dp)
            )
        }

        Button(
            onClick = {
                coroutineScope.launch {
                    SelectedPodcastStore.saveSelectedPodcasts(context, selectedPodcasts.toSet())
                    navController.navigate(Routes.HOME)
                    Log.d("selectedPodcasts", selectedPodcasts.toString())
                }
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
            Text("BẮT ĐẦU", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }
    }
}