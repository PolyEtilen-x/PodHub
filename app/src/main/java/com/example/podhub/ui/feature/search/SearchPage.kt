package com.example.podhub.ui.feature.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.rememberAsyncImagePainter
import com.example.podhub.data.PodcastResponse
import com.example.podhub.models.PodcastResponseData
import com.example.podhub.components.BottomNavigationBar
import com.example.podhub.components.HomeHeader
import com.example.podhub.storage.DataStoreManager

@Composable
fun SearchScreen(navController: NavHostController) {
    val context = LocalContext.current
    val dataStore = remember { DataStoreManager(context) }
    val userData by dataStore.userData.collectAsState(initial = emptyMap())
    var query by remember { mutableStateOf("") }
    val allPodcasts = PodcastResponse.podcastList.orEmpty()
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = navBackStackEntry?.destination?.route ?: ""

    val results = remember(query) {
        if (query.isBlank()) emptyList()
        else allPodcasts.filter {
            it.trackName?.contains(query, ignoreCase = true) == true ||
                    it.artistName?.contains(query, ignoreCase = true) == true
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
                        .fillMaxWidth() ,
                    contentAlignment = Alignment.Center
                ){
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

            if (results.isEmpty() && query.isNotBlank()) {
                Text("Không tìm thấy kết quả.", color = Color.Gray)
            }

            results.forEach { podcast ->
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
            painter = rememberAsyncImagePainter(podcast.channelImage),
            contentDescription = podcast.trackName,
            modifier = Modifier
                .size(64.dp)
        )

        Column {
            Text(
                podcast.trackName.toString(),
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
