package com.example.podhub.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VideoCall
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.vector.ImageVector

data class NavItem(
    val label: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    onHomeClick: () -> Unit,
    onSearchClick: () -> Unit,
    onRoomClick: () -> Unit,
    onLibraryClick: () -> Unit
) {
    var selectedIndex by remember { mutableStateOf(0) }

    val items = listOf(
        NavItem("Home", Icons.Filled.Home, onHomeClick),
        NavItem("Search", Icons.Filled.Search, onSearchClick),
        NavItem("Room", Icons.Filled.VideoCall, onRoomClick),
        NavItem("Your Library", Icons.Filled.LibraryBooks, onLibraryClick)
    )

    NavigationBar(
        modifier = modifier,
        containerColor = Color(0xFFFFC533),
        tonalElevation = 8.dp
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedIndex == index,
                onClick = {
                    selectedIndex = index
                    item.onClick()
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = Color.White
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
            )
        }
    }
}
