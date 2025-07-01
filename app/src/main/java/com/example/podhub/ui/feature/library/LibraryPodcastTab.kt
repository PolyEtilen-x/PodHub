package com.example.podhub.ui.feature.library

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
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.podhub.data.PodcastResponse
import com.example.podhub.models.PodcastResponseData
import com.example.podhub.viewmodels.LibraryViewModel

@Composable
fun LibraryPodcastsTab(
    libraryViewModel: LibraryViewModel = viewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    val loaded = remember { mutableStateOf(false) }
    if (!loaded.value) {
        libraryViewModel.loadInitialPodcasts(PodcastResponse.podcastList.orEmpty())
        loaded.value = true
    }

    val podcastList by libraryViewModel.podcasts.collectAsState()
    var deletedPodcastName by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(deletedPodcastName) {
        deletedPodcastName?.let {
            snackbarHostState.showSnackbar(
                message = "Đã xóa podcast: $it",
                withDismissAction = true
            )
            deletedPodcastName = null
        }
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
                PodcastLibraryItem(podcast) {
                    libraryViewModel.removePodcast(podcast)
                    deletedPodcastName = podcast.trackName
                }
            }
        }
    }
}


@Composable
fun PodcastLibraryItem(
    podcast: PodcastResponseData,
    onDeleteClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .clickable { /* điều hướng hoặc phát */ }
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(podcast.channelImage),
            contentDescription = "Podcast Image",
            modifier = Modifier
                .size(48.dp)
                .padding(end = 8.dp),
            contentScale = ContentScale.Crop
        )

        Text(
            text = podcast.trackName.orEmpty(),
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
