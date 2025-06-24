package com.example.podhub.ui.feature.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.podhub.components.BottomNavigationBar
import com.example.podhub.components.HomeHeader
import com.example.podhub.data.SamplePodcasts
import com.example.podhub.models.Podcast
import com.example.podhub.storage.DataStoreManager
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import com.example.podhub.components.ArtistRow
import com.example.podhub.components.FilterBar
import com.example.podhub.components.FilterHome
import com.example.podhub.ui.components.PodcastRow
import com.example.podhub.data.SampleArtists
import com.example.podhub.models.Artist

fun LazyListScope.podcastSection(title: String, list: List<Podcast>) {
    val columnChunks = list.chunked(2) // mỗi column có 2 dòng podcast

    item {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
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
                    PodcastRow(podcasts = columnPodcasts)
                }
            }
        }
    }
}


fun LazyListScope.artistSection(title: String, list: List<Artist>) {
    val artistColumns = list.chunked(2)

    item {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
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



@Composable
fun HomeScreen(navController: NavHostController) {
    val context = LocalContext.current
    val dataStore = remember { DataStoreManager(context) }
    val userData by dataStore.userData.collectAsState(initial = emptyMap())
    Log.d("DataStore", "userData = $userData")

    val recentPodcasts = SamplePodcasts.podcastList
    val yourPlaylist = SamplePodcasts.podcastList //yourPlaylist
    val suggestList = SamplePodcasts.podcastList //suggestList
    val popularList = SamplePodcasts.podcastList //popularList
    val artists = SampleArtists.artistList //artists


    Scaffold(
        topBar = {
            HomeHeader(
                userName = userData["name"] ?: "Guest",
                userAvatarUrl = userData["photoUrl"] ?: ""
            )
        },
        bottomBar = {
            BottomNavigationBar(
                onHomeClick = { /* navController.navigate("home") */ },
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
                    onFilterSelected = { selected ->
                        // TODO: xử lý chọn filter ở đây
                    }
                )
            }

            podcastSection("Recent", recentPodcasts)
            podcastSection("Your Playlist", yourPlaylist)
            podcastSection("Suggest for you", suggestList)
            podcastSection("Popular Podcasts", popularList)
            artistSection("Top Artists", artists)
        }

    }

}
