package com.example.podhub.ui.feature.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.podhub.RequestModel.HistoryRequest
import com.example.podhub.components.BottomNavigationBar
import com.example.podhub.components.HomeHeader
import com.example.podhub.models.PodcastResponseData
import com.example.podhub.storage.DataStoreManager
import com.example.podhub.ui.feature.search.shimmerBrush
import com.example.podhub.ui.navigation.Routes
import com.example.podhub.viewmodels.FavouriteViewModel
import com.example.podhub.viewmodels.HistoryViewModel
import com.example.podhub.viewmodels.PodcastViewModel


@SuppressLint("UnrememberedMutableState")
@Composable
fun PodcastDetailScreen(
    navController: NavHostController,
    podcast: PodcastResponseData,
    podcastViewModel: PodcastViewModel,
    favouriteViewModel: FavouriteViewModel,
    isHistory : Boolean = false,
    historyViewModel: HistoryViewModel
) {
    val context = LocalContext.current
    val dataStore = remember { DataStoreManager(context) }
    val userData by dataStore.userData.collectAsState(initial = emptyMap())
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = navBackStackEntry?.destination?.route ?: ""
    val episodes by podcastViewModel.episodes.collectAsState()
    val isFavourite by favouriteViewModel.isFavourite.collectAsState()
    val buttonColor = if (isFavourite) Color(0xFFFF4081) else Color.White
    val iconColor = if (isFavourite) Color.White else Color.Gray
    val icon = if (isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder
    // History dialog state
    var showHistoryDialog by remember { mutableStateOf(false) }
    var episodeIndex by remember { mutableStateOf<Int?>(null) }
    var recentPlayedSeconds by remember { mutableStateOf<Int?>(null) }
    val isLoading by podcastViewModel.isLoading.collectAsState()

    fun formatDurationFromMillis(milliseconds: Int): String {
        val totalSeconds = milliseconds / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%d:%02d", minutes, seconds)
    }


    if (isHistory) {
        LaunchedEffect(Unit) {
            val episodeIdx = historyViewModel.getEpisodeIndex()
            val recentPlayed = historyViewModel.getRecentPlayed()

            if (episodeIdx != null && recentPlayed != null) {
                episodeIndex = episodeIdx
                recentPlayedSeconds = recentPlayed
                showHistoryDialog = true
            }
        }
    }
    LaunchedEffect(Unit) {
        podcastViewModel.fetchEpisodesPodCast(podcast.feedUrl.toString())
        podcast.trackId?.let { favouriteViewModel.checkIfPodcastFavourited("332211",it) }



    }
    LaunchedEffect(navController.currentBackStackEntry?.savedStateHandle?.get<Boolean>("dismissHistoryDialog")) {
        val dismissed = navController.currentBackStackEntry
            ?.savedStateHandle
            ?.get<Boolean>("dismissHistoryDialog") ?: false

        if (dismissed) {
            showHistoryDialog = false
            navController.currentBackStackEntry?.savedStateHandle?.remove<Boolean>("dismissHistoryDialog")
        }
    }


    // History Dialog
    if (showHistoryDialog && episodeIndex != null && recentPlayedSeconds != null && episodes.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = { showHistoryDialog = false },
            title = {
                Text(
                    text = "Tiếp tục nghe?",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFC533)
                )
            },
            text = {
                Text(
                    text = "Bạn có muốn tiếp tục nghe từ tập ${episodes.get(episodeIndex!!).title} tại ${formatDurationFromMillis(recentPlayedSeconds!!)}?",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showHistoryDialog = false

                        navController.currentBackStackEntry?.savedStateHandle?.apply {
                            set("recentPlay",recentPlayedSeconds)
                            set("initialEpisodeIndex", episodeIndex)
                            set("imageUrl", podcast.artworkUrl100)
                            set("artistName", podcast.artistName)
                            set("podCastId",podcast.trackId)
                        }
                        navController.navigate(Routes.PLAYER)
                    }
                ) {
                    Text("Có", color = Color(0xFFFFC533))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showHistoryDialog = false }
                ) {
                    Text("Không", color = Color.Gray)
                }
            },
            containerColor = Color.White
        )
    }

    Scaffold(
        topBar = {
            Column {
                HomeHeader(
                    userName = userData["name"] ?: "Guest",
                    userAvatarUrl = userData["photoUrl"] ?: ""
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp)
                ) {
                    IconButton(onClick = {

                        if (historyViewModel.getlastIndex() != null && historyViewModel.getRecentPlay() !=null && isHistory==false){
                            Log.d("tracker lastindex",historyViewModel.getlastIndex().toString())
                            Log.d("tracker recent",  HistoryRequest(
                                podcast.trackId!!,
                                historyViewModel.getRecentPlay()!!,
                                historyViewModel.getlastIndex()!!
                            ).toString())
                        historyViewModel.postHistory(
                            "1111",
                            HistoryRequest(
                                podcast.trackId!!,
                                historyViewModel.getRecentPlay()!!,
                                historyViewModel.getlastIndex()!!
                            )
                        )}
                        historyViewModel.clear()

                        navController.popBackStack() }) {
                        Icon(
                            tint = Color(0xFFFFC533),
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            }
        },
        bottomBar = {
            BottomNavigationBar(
                currentRoute = currentRoute,
                onHomeClick = { navController.navigate("home") },
                onSearchClick = { navController.navigate("search")},
                onRoomClick = { navController.navigate("room") },
                onLibraryClick = { navController.navigate("library") }
            )
        },
        containerColor = Color(0xFFFDF8F3)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = podcast.artworkUrl100,
                        contentDescription = podcast.collectionName,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )


                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = podcast.trackName.toString(),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFC533)
                    )

                    Text(
                        text = "Podcaster: ${podcast.artistName}",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            navController.currentBackStackEntry?.savedStateHandle?.apply {
                                set("recentPlay",0)
                                set("initialEpisodeIndex", 0)
                                set("imageUrl", podcast.artworkUrl100)
                                set("artistName", podcast.artistName)
                                set("podCastId",podcast.trackId)
                            }
                            navController.navigate(Routes.PLAYER)
                        }
                        ,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC533)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Phát", color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {

                            podcast.trackId?.let { favouriteViewModel.toggleFavourite("332211", it) }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(48.dp)
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = "Add to Favorites",
                            tint = iconColor
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Yêu thích", color = iconColor)
                    }


                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Chương:",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFC533)
                    )
                }
            }

            if (isLoading) {
                items(8) { EpisodeShimmerItem() } // show 8 shimmer items while loading
            } else {
                itemsIndexed(episodes) { index, episode ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val imageUrl = if (episode.image != "") episode.image else podcast.artworkUrl100

                        Image(
                            painter = rememberAsyncImagePainter(imageUrl),
                            contentDescription = episode.title,
                            modifier = Modifier
                                .size(56.dp)
                                .padding(end = 12.dp)
                                .clickable {
                                    navController.currentBackStackEntry?.savedStateHandle?.apply {
                                        set("recentPlay", 0)
                                        set("initialEpisodeIndex", index)
                                        set("imageUrl", imageUrl)
                                        set("artistName", podcast.artistName)
                                        set("podCastId",podcast.trackId)
                                    }
                                    navController.navigate(Routes.PLAYER)
                                },
                            contentScale = ContentScale.Crop
                        )

                        Column {
                            Text(
                                text = episode.title.toString(),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFC533)
                            )
                            Text(
                                text = episode.description.toString(),
                                fontSize = 13.sp,
                                color = Color.Gray,
                                maxLines = 2
                            )
                        }
                    }
                }
            }
            }
        }
    }


@Composable
fun EpisodeShimmerItem() {
    val shimmer = shimmerBrush()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(shimmer)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .height(16.dp)
                    .fillMaxWidth(0.5f)
                    .clip(RoundedCornerShape(4.dp))
                    .background(shimmer)
            )

            Box(
                modifier = Modifier
                    .height(12.dp)
                    .fillMaxWidth(0.8f)
                    .clip(RoundedCornerShape(4.dp))
                    .background(shimmer)
            )
        }
    }
}
