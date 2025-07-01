package com.example.podhub.ui.feature.library

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.podhub.models.Playlist
import com.example.podhub.models.PodcastResponseData

@Composable
fun PlaylistDetailScreen(playlist: Playlist) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Image(
                painter = rememberAsyncImagePainter(model = playlist.imageUrl),
                contentDescription = "Playlist Cover",
                modifier = Modifier
                    .size(220.dp)
                    .padding(bottom = 16.dp)
            )

            Text(
                text = playlist.title,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFC533)
            )

            Text(
                text = "soft, chill, dreamy, lo-fi beats",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }

        items(playlist.podcasts) { podcast ->
            PodcastItem(podcast)
        }
    }
}


@Composable
fun PodcastItem(podcast: PodcastResponseData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(podcast.channelImage),
            contentDescription = "Podcast Image",
            modifier = Modifier
                .size(48.dp)
                .padding(end = 12.dp)
        )
        Column {
            Text(
                text = podcast.trackName ?: "No title",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFFFFC533)
            )
            Text(
                text = podcast.artistName ?: "Unknown artist",
                fontSize = 13.sp,
                color = Color.Gray
            )
        }
    }
}
