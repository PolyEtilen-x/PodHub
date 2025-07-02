package com.example.podhub.ui.feature.room

import android.os.Bundle
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.podhub.models.RoomInfo
import com.example.podhub.models.UserModel
import com.example.podhub.storage.DataStoreManager
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import com.example.podhub.components.BottomNavigationBar
import com.example.podhub.components.HomeHeader

@Composable
fun NewRoomScreen(navController: NavController, )
{
    val context = LocalContext.current
    val dataStore = remember { DataStoreManager(context) }
    val userData by dataStore.userData.collectAsState(initial = emptyMap())

    var roomTopic by remember { mutableStateOf("") }
    var isCreatingRoom by remember { mutableStateOf(false) }

    val onCreateRoom = {
        if (roomTopic.isNotEmpty()) {
            isCreatingRoom = true

            val newRoom = RoomInfo(
                id = generateRoomId(),
                topic = roomTopic,
                creator = UserModel(
                    uid = userData["uid"] ?: "",
                    displayName = userData["name"] ?: "Guest"
                ),
                participants = listOf(userData["uid"] ?: "")

            )
            navController.navigate("livestream/${newRoom.id}")
        } else {
            Toast.makeText(context, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show()
        }
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
                currentRoute = "newroom",
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
                .padding(16.dp)
        ) {
            Text(
                text = "Tạo phòng mới",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFC533),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = roomTopic,
                onValueChange = { roomTopic = it },
                label = { Text("Chủ đề") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onCreateRoom,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC533))
            ) {
                if (isCreatingRoom) {
                    Text("Đang tạo phòng...")
                } else {
                    Text("Tạo phòng")
                }
            }
        }
    }
}

fun generateRoomId(): String {
    return "room_${System.currentTimeMillis()}"
}