package com.example.podhub.ui.feature.artist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.podhub.R
import com.example.podhub.data.SampleArtists
import com.example.podhub.components.ArtistItem
import com.example.podhub.ui.navigation.Routes

@Composable
fun ArtistSelectionScreen(navController: NavHostController) {
    var searchQuery by remember { mutableStateOf("") }

    val artists = SampleArtists.artistList

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAF6F5))
            .padding(horizontal = 24.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.Fit
        )

        // Title
        Text(
            text = "Artists you like",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFFC533)
        )

        Divider(color = Color(0xFFFFC533), thickness = 2.dp)

        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFFC107),
                unfocusedBorderColor = Color(0xFFFFC107),
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(artists) { artist ->
                ArtistItem(
                    name = artist.name,
                    imageUrl = artist.imageUrl
                )
            }
        }

        Button(
            onClick = {
                navController.navigate(Routes.FAVORITE_PODCAST)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFC533),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(text = "NEXT", fontWeight = FontWeight.Bold, fontSize = 30.sp)
        }
    }
}
