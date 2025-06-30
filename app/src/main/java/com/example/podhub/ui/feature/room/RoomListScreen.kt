package com.example.podhub.ui.feature.room

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.podhub.components.BottomNavigationBar
import com.example.podhub.components.HomeHeader
import com.example.podhub.data.PodcastResponse
import com.example.podhub.models.RoomInfo
import com.example.podhub.storage.DataStoreManager
import com.example.podhub.ui.feature.search.SearchResultItem
import com.example.podhub.viewmodels.RoomViewModel
import kotlin.collections.orEmpty

@Composable
fun RoomListScreen(
    navController: NavHostController,
    viewModel: RoomViewModel = hiltViewModel()
) {
    val roomList = viewModel.roomList
    val currentUserId = viewModel.currentUserId
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
            it.creatorName.contains(query, ignoreCase = true) ||
                    it.artistName.contains(query, ignoreCase = true)
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
                        "Phòng nghe Podcast",
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
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            items(roomList) { room ->
                RoomItem(room = room) {
                    if (room.creatorName == currentUserId) {
                        navController.navigate("livestream/${room.id}")
                    } else {
                        navController.navigate("listen/${room.id}")
                    }
                }
            }
        }
    }
}

@Composable
fun RoomItem(room: RoomInfo, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Chủ đề: ${room.topic}")
            Text(text = "Người tạo: ${room.creatorName}")
            Text(text = "Trạng thái: ${room.status}")
        }
    }
}
