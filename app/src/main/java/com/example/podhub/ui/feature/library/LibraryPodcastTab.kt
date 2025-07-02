package com.example.podhub.ui.feature.library

import android.R.attr.onClick
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.podhub.data.PodcastResponse
import com.example.podhub.models.PodcastResponseData
import com.example.podhub.storage.DataStoreManager
import com.example.podhub.ui.navigation.Routes
import com.example.podhub.viewmodels.FavouriteViewModel
import com.example.podhub.viewmodels.LibraryViewModel
import com.example.podhub.viewmodels.PodcastViewModel
import kotlinx.coroutines.launch

@Composable
fun LibraryPodcastsTab(
    podcastViewModel: PodcastViewModel,
    favouriteViewModel: FavouriteViewModel,
    navController: NavHostController
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val isLoading by favouriteViewModel.isLoading.collectAsState()


    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val dataStore = remember { DataStoreManager(context) }

    val podcastList by podcastViewModel.favouritePodcasts.collectAsState()
    LaunchedEffect(Unit) {
        podcastViewModel.fetchFavouritePodCast(dataStore.getUid())
    }

    LaunchedEffect(isLoading) {
        podcastViewModel.fetchFavouritePodCast(dataStore.getUid())
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
            items(podcastList) { podcast ->
                PodcastLibraryItem(podcast, onDeleteClick = {
                    podcast.trackId?.let { trackId ->
                        scope.launch {
                            favouriteViewModel.toggleFavourite(dataStore.getUid(), trackId)
                            podcastViewModel.fetchFavouritePodCast(dataStore.getUid())
                        }
                    }
                }, navController
                )
            }
        }
    }
}


@Composable
fun PodcastLibraryItem(
    podcast: PodcastResponseData,
    onDeleteClick: () -> Unit,
    navController: NavHostController,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .clickable {
                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.set("podcast", podcast)


                navController.navigate(Routes.PODCAST_DETAIL)
            }
            .padding(horizontal = 8.dp, vertical = 10.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(podcast.artworkUrl100),
            contentDescription = "Podcast Image",
            modifier = Modifier
                .size(56.dp)
                .padding(end = 12.dp),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        ) {
            Text(
                text = podcast.trackName.orEmpty(),
                fontSize = 15.sp,
                color = Color.Black,
                maxLines = 2
            )
            Text(
                text = podcast.artistName ?: "",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        OutlinedButton(
            onClick = onDeleteClick,
            border = BorderStroke(1.dp, Color.Red),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
            shape = RoundedCornerShape(50),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
            modifier = Modifier.height(36.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = Color.Red,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Xóa", fontSize = 12.sp)
        }
    }
}
