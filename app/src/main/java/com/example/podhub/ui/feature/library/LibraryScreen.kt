package com.example.podhub.ui.feature.library

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.podhub.R
import com.example.podhub.components.BottomNavigationBar
import com.example.podhub.viewmodels.ArtistViewModel
import com.example.podhub.viewmodels.FavouriteViewModel
import com.example.podhub.viewmodels.PodcastViewModel
import com.example.podhub.viewmodels.SharedPlaylistViewModel

@Composable
fun LibraryScreen(navController: NavHostController,podcastViewModel: PodcastViewModel,artistViewModel: ArtistViewModel,favouriteViewModel: FavouriteViewModel) {
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = navBackStackEntry?.destination?.route ?: ""

    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Podcasts", "Artists", "Playlists")

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentRoute = currentRoute,
                onHomeClick = { navController.navigate("home") },
                onSearchClick = { navController.navigate("search") },
                onRoomClick = { navController.navigate("room") },
                onLibraryClick = { navController.navigate("library") }
            )
        },
        containerColor = Color(0xFFFDF8F3)
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .width(150.dp)
                        .height(35.dp),
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = "Your Library",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color(0xFFFFC533)
                )
            }


            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.White,
                contentColor = Color(0xFFFFC533)
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = FontWeight.Bold,
                                color = if (selectedTabIndex == index) Color.White else Color(0xFFFFC533)
                            )
                        },
                        modifier = Modifier.background(
                            if (selectedTabIndex == index) Color(0xFFFFC533) else Color.White
                        )
                    )
                }
            }

            when (selectedTabIndex) {
                0 -> LibraryPodcastsTab(podcastViewModel,favouriteViewModel, navController)
                1 -> LibraryArtistsTab(artistViewModel,favouriteViewModel)
                2 -> LibraryPlaylistsTab(navController)
            }
        }
    }
}
