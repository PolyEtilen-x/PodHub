package com.example.podhub.ui.feature.library

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import coil.compose.rememberAsyncImagePainter
import com.example.podhub.components.BottomNavigationBar
import com.example.podhub.data.PodcastResponse
import com.example.podhub.models.PodcastResponseData
import com.example.podhub.R
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun LibraryPodcastsScreen(navController: NavHostController) {
    val podcastList = PodcastResponse.podcastList.orEmpty()
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = navBackStackEntry?.destination?.route ?: ""

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentRoute = currentRoute,
                onHomeClick = { navController.navigate("home") },
                onSearchClick = { navController.navigate("search") },
                onRoomClick = { navController.navigate("room") },
                onLibraryClick = {navController.navigate("library")}
            )
        },
        containerColor = Color(0xFFFDF8F3)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .width(150.dp)
                        .height(35.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Your Library",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color(0xFFFFC533)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                LibraryTabItem("Podcasts", true)
                LibraryTabItem("Arstics", false)
                LibraryTabItem("Album", false)
                LibraryTabItem("Playlists", false)
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(podcastList.size / 2) { rowIndex ->
                    val index1 = rowIndex * 2
                    val index2 = index1 + 1

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        PodcastLibraryItem(podcastList[index1])

                        if (index2 < podcastList.size) {
                            PodcastLibraryItem(podcastList[index2])
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LibraryTabItem(title: String, isSelected: Boolean) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) Color(0xFFFFC533) else Color.White,
        shadowElevation = 2.dp
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            color = if (isSelected) Color.White else Color(0xFFFFC533),
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun PodcastLibraryItem(podcast: PodcastResponseData) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(64.dp)
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .clickable { }
            .padding(end = 8.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(podcast.channelImage),
            contentDescription = "Podcast Thumbnail",
            modifier = Modifier
                .size(48.dp)
                .padding(start = 8.dp),
            contentScale = ContentScale.Crop
        )

        Text(
            text = podcast.trackName,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = Color.Black,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .weight(1f)
        )

        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFFFFC533),
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .defaultMinSize(minHeight = 32.dp)
        ) {
            Text(
                text = "Xóa",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}
