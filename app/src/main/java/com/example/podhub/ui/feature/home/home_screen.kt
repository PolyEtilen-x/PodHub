package com.example.podhub.ui.feature.home

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.podhub.components.BottomNavigationBar
import com.example.podhub.components.HomeHeader
import com.example.podhub.components.FilterBar
import com.example.podhub.components.FilterHome
import com.example.podhub.data.PodcastResponse
import com.example.podhub.models.Artist
import com.example.podhub.models.PodcastResponseData
import com.example.podhub.storage.DataStoreManager
import com.example.podhub.ui.components.MiniPlayerBar
import com.example.podhub.ui.components.PodcastRow
import com.example.podhub.ui.feature.home.PodcastCategoriesScreen
import com.example.podhub.viewmodel.PlayerViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.podhub.components.ArtistRow
import com.example.podhub.ui.navigation.Routes
import com.example.podhub.viewmodels.ArtistViewModel
import com.example.podhub.viewmodels.FavouriteViewModel
import com.example.podhub.viewmodels.HistoryViewModel
import com.example.podhub.viewmodels.PodcastViewModel

@Composable
fun HomeScreen(navController: NavHostController,artistViewModel: ArtistViewModel,podcastViewModel: PodcastViewModel,favouriteViewModel: FavouriteViewModel,historyViewModel: HistoryViewModel) {
    val context = LocalContext.current
    val dataStore = remember { DataStoreManager(context) }
    val userData by dataStore.userData.collectAsState(initial = emptyMap())
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = navBackStackEntry?.destination?.route ?: ""

    val selectedFilter = rememberSaveable { mutableStateOf(FilterHome.All) }

    val recentPodcasts = PodcastResponse.podcastList.orEmpty()
    val yourPlaylist = PodcastResponse.podcastList
    val suggestList by podcastViewModel.podcasts.collectAsState()
    val popularList = PodcastResponse.podcastList
    val artists by artistViewModel.artists.collectAsState()
    val suggestedPodcast by favouriteViewModel.suggestedPodcasts.collectAsState()
    val historyPodcasts by historyViewModel.podcastIds.collectAsState()


    LaunchedEffect(Unit) {
        Log.d("uuid",dataStore.getUid())

        favouriteViewModel.loadSuggestedPodcasts(dataStore.getUid())
        historyViewModel.loadHistory(dataStore.getUid())
    }


    Scaffold(
        topBar = {
            HomeHeader(
                userName = userData["name"] ?: "Guest",
                userAvatarUrl = userData["photoUrl"] ?: ""
            )
        },
        bottomBar = {
            BottomNavigationBar(
                currentRoute = currentRoute,
                onHomeClick = { navController.navigate("home") },
                onSearchClick = { navController.navigate("search") },
                onRoomClick = { navController.navigate("room") },
                onLibraryClick = { navController.navigate("library") }
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            item {
                FilterBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    selectedFilter = selectedFilter.value,
                    onFilterSelected = { selected -> selectedFilter.value = selected }
                )
            }

            val onPodcastClick: (PodcastResponseData, Boolean) -> Unit = { podcast, isHistory ->
                if (isHistory) {
                    val indexInRecent = historyPodcasts.indexOf(podcast)
                    if (indexInRecent != -1) {
                        historyViewModel.setSelectedIndex(indexInRecent)
                    }
                }

                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.set("podcast", podcast)

                // Save the isHistory flag as well
                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.set("isHistory", isHistory)

                navController.navigate(Routes.PODCAST_DETAIL)
            }

            when (selectedFilter.value) {
                FilterHome.All -> {
                    podcastSection("Gần đây", historyPodcasts, onPodcastClick, isHistory = true)
                    podcastSection("Danh sách phát", yourPlaylist, onPodcastClick, isHistory = false)
                    podcastSection("Đề xuất", suggestedPodcast, onPodcastClick, isHistory = false)
                    podcastSection("Podcast phổ biến", popularList, onPodcastClick, isHistory = false)
                    artistSection("Nghệ sĩ nổi bật", artists)
                }
                FilterHome.Podcasts -> {
                    item {
                        PodcastCategoriesScreen(navController,podcastViewModel)
                    }
                }
                FilterHome.Artists -> {
                    item {
                        PodcasterScreen(navController = navController, artists = artists)
                    }
                }
            }
        }
    }
}

fun LazyListScope.podcastSection(
    title: String,
    list: List<PodcastResponseData>,
    onItemClick: (PodcastResponseData, Boolean) -> Unit,
    isHistory: Boolean = false // Default is false
) {
    val columnChunks = list.chunked(2)
    item {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = title,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFC533)
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(columnChunks) { columnPodcasts ->
                    PodcastRow(
                        podcasts = columnPodcasts,
                        onItemClick = { podcast ->
                            onItemClick(podcast, isHistory)
                        }
                    )
                }
            }
        }
    }
}

fun LazyListScope.artistSection(title: String, list: List<Artist>) {
    val artistColumns = list.chunked(1)
    item {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = title,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFC533)
            )
            LazyRow {
                items(artistColumns) { columnArtists ->
                    ArtistRow(columnArtists)
                }
            }
        }
    }
}
