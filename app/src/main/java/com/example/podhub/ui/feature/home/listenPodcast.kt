package com.example.podhub.ui.feature.home

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.podhub.models.PodcastResponseData
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.podhub.models.Episode
import com.example.podhub.viewmodels.PodcastViewModel
import kotlinx.coroutines.delay

@Composable
fun PlayerScreen(
    navController: NavHostController,
    podcastViewModel: PodcastViewModel,
    initialEpisodeIndex: Int,
    imageUrl: String,
    artistName: String
) {
    val context = LocalContext.current
    val episodes by podcastViewModel.episodes.collectAsState()

    // State for current episode index
    var currentEpisodeIndex by remember { mutableStateOf(initialEpisodeIndex) }

    // Get current episode safely
    val currentEpisode = episodes.getOrNull(currentEpisodeIndex)

    var isPlaying by remember { mutableStateOf(false) }
    var sliderPosition by remember { mutableStateOf(0f) }
    // --- ExoPlayer setup ---
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build()
    }
    suspend fun trackPlayback() {

        while (!exoPlayer.isPlaying) {
            delay(100)
        }

        while (exoPlayer.isPlaying) {
            val position = exoPlayer.currentPosition
            val duration = exoPlayer.duration.takeIf { it > 0 } ?: 1
            sliderPosition = (position.toFloat() / duration).coerceIn(0f, 1f)
            delay(500)
        }
    }



    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    LaunchedEffect(currentEpisodeIndex) {
        currentEpisode?.let { episode ->
            // Stop current playback
            exoPlayer.stop()
            exoPlayer.clearMediaItems()

            // Reset player state variables
            sliderPosition = 0f
            isPlaying = false

            // Assuming Episode has an audioUrl property
            val mediaItem = MediaItem.fromUri(Uri.parse(episode.audioUrl))
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            exoPlayer.play()


            isPlaying = true


            trackPlayback()
        }
    }



    LaunchedEffect(isPlaying) {
       trackPlayback()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.align(Alignment.Start)
        ) {
            Icon(
                Icons.Default.ExpandMore,
                contentDescription = "Collapse",
                tint = Color(0xFFFFC533)
            )
        }

        Image(
            painter = rememberAsyncImagePainter(
                imageUrl
            ),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(bottom = 16.dp),
            contentScale = ContentScale.Crop
        )

        Text(
            text = currentEpisode?.title ?: "Unknown Podcast",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFFC533)
        )
        Text(
            text = artistName ?: "Unknown Artist",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )

        currentEpisode?.title?.let {
            Text(
                text = it,
                fontSize = 17.sp,
                color = Color(0xFFFFC533),
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Slider(
            value = sliderPosition,
            onValueChange = { ratio ->
                val seekPosition = (ratio * exoPlayer.duration).toLong()
                exoPlayer.seekTo(seekPosition)
                sliderPosition = ratio
            },
            modifier = Modifier.fillMaxWidth(),
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFFFFC533),
                activeTrackColor = Color(0xFFFFC533)
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(formatTime(exoPlayer.currentPosition), color = Color.Gray)
            Text(formatTime(if (exoPlayer.duration > 0) exoPlayer.duration else 0), color = Color.Gray)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { /* Shuffle not handled */ }) {
                Icon(Icons.Default.Shuffle, contentDescription = "Shuffle", tint = Color.Gray)
            }

            IconButton(
                onClick = {
                    // Go to previous episode
                    if (currentEpisodeIndex > 0) {
                        currentEpisodeIndex--
                    }
                },
                enabled = currentEpisodeIndex > 0 // Disable if at first episode
            ) {
                Icon(
                    Icons.Default.FastRewind,
                    contentDescription = "Previous",
                    tint = if (currentEpisodeIndex > 0) Color(0xFFFFC533) else Color.Gray
                )
            }

            IconButton(
                onClick = {
                    if (exoPlayer.isPlaying) {
                        exoPlayer.pause()
                        isPlaying = false
                    } else {
                        exoPlayer.play()
                        isPlaying = true
                    }
                },
                modifier = Modifier
                    .size(72.dp)
                    .background(Color(0xFFFFC533), shape = RoundedCornerShape(50))
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = "Play/Pause",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }

            IconButton(
                onClick = {
                    // Go to next episode
                    if (currentEpisodeIndex < episodes.size - 1) {
                        currentEpisodeIndex++
                    }
                },
                enabled = currentEpisodeIndex < episodes.size - 1 // Disable if at last episode
            ) {
                Icon(
                    Icons.Default.FastForward,
                    contentDescription = "Next",
                    tint = if (currentEpisodeIndex < episodes.size - 1) Color(0xFFFFC533) else Color.Gray
                )
            }

            IconButton(onClick = { /* Repeat not handled */ }) {
                Icon(Icons.Default.Repeat, contentDescription = "Repeat", tint = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text("DESCRIPTION", color = Color.Gray, fontSize = 12.sp)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFFC533), shape = RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Text(
                text = currentEpisode?.description ?: "Không có lời mô tả.",
                color = Color.White
            )
        }
    }
}

fun formatTime(ms: Long): String {
    val totalSec = ms / 1000
    val min = totalSec / 60
    val sec = totalSec % 60
    return "%d:%02d".format(min, sec)
}