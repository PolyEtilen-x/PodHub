package com.example.podhub.ui.feature.library

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.podhub.R
import com.example.podhub.models.Artist
import com.example.podhub.viewmodels.ArtistViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.podhub.storage.DataStoreManager
import com.example.podhub.viewmodels.FavouriteViewModel

@Composable
fun LibraryArtistsTab(
    artistViewModel: ArtistViewModel,
    favouriteViewModel: FavouriteViewModel
) {
    val allArtists by artistViewModel.FavourtiteArtists.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val context = LocalContext.current
    val dataStore = remember { DataStoreManager(context) }


    LaunchedEffect(Unit) {
        artistViewModel.fetchFavouriteArtist(dataStore.getUid())
        Log.d("artists",allArtists.toString())
    }


    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            items(allArtists) { artist ->
                ArtistLibraryItem(artist = artist, onDeleteClick = {})
            }
        }
    }
}


@Composable
fun ArtistLibraryItem(
    artist: Artist,
    onDeleteClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .clickable { /* Mở detail nếu có */ }
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(artist.avatar ?: R.drawable.avatar_default),
            contentDescription = "Artist Avatar",
            modifier = Modifier
                .size(48.dp)
                .padding(end = 8.dp),
            contentScale = ContentScale.Crop
        )

        Text(
            text = artist.name.orEmpty(),
            fontSize = 14.sp,
            color = Color.Black,
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        )

        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFFFFC533),
            modifier = Modifier
                .defaultMinSize(minHeight = 32.dp)
                .clickable { onDeleteClick() }
        ) {
            Text(
                text = "Xóa",
                color = Color.White,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
    }
}
