package com.example.podhub.ui.feature.search

import android.annotation.SuppressLint
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.rememberAsyncImagePainter
import com.example.podhub.models.PodcastResponseData
import com.example.podhub.components.BottomNavigationBar
import com.example.podhub.components.HomeHeader
import com.example.podhub.storage.DataStoreManager
import com.example.podhub.viewmodels.PodcastViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.items


@Composable
fun SearchScreen(navController: NavHostController, podcastViewModel: PodcastViewModel) {
    val context = LocalContext.current
    val dataStore = remember { DataStoreManager(context) }
    val userData by dataStore.userData.collectAsState(initial = emptyMap())
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = navBackStackEntry?.destination?.route ?: ""
    var query by remember { mutableStateOf("") }
    val searchResults by podcastViewModel.searchResults.collectAsState()
    val isLoading by podcastViewModel.isLoading.collectAsState()

    // Launch search on query input
    LaunchedEffect(query) {
        if (query.isNotBlank()) {
            delay(300) // debounce input
            podcastViewModel.searchPodcasts(query)
        }
    }

    Scaffold(
        topBar = {
            Column {
                HomeHeader(
                    userName = userData["name"] ?: "Guest",
                    userAvatarUrl = userData["photoUrl"] ?: ""
                )
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Tìm kiếm Podcast",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFC533)
                    )
                }
            }
        },
        bottomBar = {
            BottomNavigationBar(
                currentRoute = currentRoute,
                onHomeClick = { navController.navigate("home") },
                onSearchClick = { navController.navigate("search") },
                onRoomClick = { navController.navigate("room") },
                onLibraryClick = { navController.navigate("library") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                placeholder = { Text("Nhập tên podcast hoặc nghệ sĩ...") },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFFC533),
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = Color(0xFFFFC533),
                )
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                when {
                    isLoading -> {
                        items(10) { SearchResultShimmerItem() }
                    }
                    searchResults.isEmpty() && query.isNotBlank() -> {
                        item {
                            Text("Không tìm thấy kết quả.", color = Color.Gray)
                        }
                    }
                    else -> {
                        items(
                            items = searchResults,
                            key = { it.trackId!! }
                        ) { podcast ->
                            SearchResultItem(podcast) {
                                navController.currentBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("podcast", podcast)
                                navController.navigate("podcast_detail")
                            }
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun SearchResultItem(
    podcast: PodcastResponseData,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(podcast.artworkUrl100),
            contentDescription = podcast.trackName,
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Column {
            Text(
                podcast.trackName ?: "Không tên",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFC533)
            )
            Text(
                "by ${podcast.artistName}",
                fontSize = 13.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun shimmerBrush(): Brush {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f)
    )
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "translate"
    )
    return Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim.value, translateAnim.value),
        end = Offset(translateAnim.value + 200f, translateAnim.value + 200f)
    )
}

@Composable
fun SearchResultShimmerItem() {
    val brush = shimmerBrush()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(brush)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(160.dp)
                    .height(20.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(brush)
            )
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(16.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(brush)
            )
        }
    }
}
