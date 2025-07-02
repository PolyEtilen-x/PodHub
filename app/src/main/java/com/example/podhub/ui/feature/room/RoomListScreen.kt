package com.example.podhub.ui.feature.room

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.podhub.components.BottomNavigationBar
import com.example.podhub.components.HomeHeader
import com.example.podhub.models.RoomInfo
import com.example.podhub.models.UserModel
import com.example.podhub.storage.DataStoreManager


@Composable
fun RoomListScreen(
    navController: NavController,
) {
    val context = LocalContext.current
    val dataStore = remember { DataStoreManager(context) }
    val userData by dataStore.userData.collectAsState(initial = emptyMap())
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = navBackStackEntry?.destination?.route ?: ""

    // Dữ liệu phòng giả (dummy data)
    val roomList = listOf(
        RoomInfo(
            id = "1",
            topic = "Podcast về công nghệ",
            creator = UserModel(uid = "user1", displayName = "Người sáng lập 1"),
            participants = listOf("user1", "user2", "user3")
        ),
        RoomInfo(
            id = "2",
            topic = "Podcast về giáo dục",
            creator = UserModel(uid = "user2", displayName = "Người sáng lập 2"),
            participants = listOf("user1", "user4")
        ),
        RoomInfo(
            id = "3",
            topic = "Podcast về âm nhạc",
            creator = UserModel(uid = "user3", displayName = "Người sáng lập 3"),
            participants = listOf("user2", "user3")
        )
    )

    val currentUserId = "user1"

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
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Text(
                text = "Phòng phát podcast",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFC533),
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)
            )

            // Nút tạo phòng
            Button(
                onClick = {
                    val appID: Long = 2113018141
                    val appSign = "8d0ab927091ce85df2f9a2e83e6b9676a7bef3233b60219b96695a74b7116129"
                    val userID = userData["uid"] ?: "Khách"
                    val userName = userData["name"] ?: "khách"
                    val liveID = generateRoomId()
                    navController.navigate("newroom")},
                modifier = Modifier
                    .width(200.dp)
                    .align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC533))
            ) {
                Text("Tạo phòng")
            }

            LazyColumn(modifier = Modifier.padding(16.dp)) {
                items(roomList.size) { index ->
                    RoomItem(room = roomList[index]) {
                        val roomId = roomList[index].id
                        val isCreator = roomList[index].creator.uid == currentUserId

                        if (isCreator) {
                            navController.navigate("livestream/$roomId")                      } else {
                            navController.navigate("listen/$roomId")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RoomItem(
    room: RoomInfo,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFC533)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(16.dp)
        ) {
            Column() {
                Text(
                    text = room.topic,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Người tạo: ${room.creator.displayName ?: "Unknown"}",
                    fontSize = 16.sp,
                    color = Color(0xFFFDF8F3),
                    fontWeight = FontWeight.W600
                )
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Số người tham gia: ${room.participants.size}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}
