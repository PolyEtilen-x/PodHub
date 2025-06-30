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
import kotlinx.coroutines.delay

@Composable
fun PlayerScreen(
    navController: NavHostController,
    podcast: PodcastResponseData
) {
    val context = LocalContext.current
    val episode = podcast.episodes?.firstOrNull()

    // --- ExoPlayer setup ---
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build()
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    // --- Prepare media once ---
    LaunchedEffect(episode) {
        if (episode != null) {
            val mediaItem = MediaItem.fromUri(Uri.parse(episode.audioUrl))
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            exoPlayer.play()
        }
    }

    var isPlaying by remember { mutableStateOf(true) }
    var sliderPosition by remember { mutableStateOf(0f) }

    // --- Update slider ---
    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            val position = exoPlayer.currentPosition
            val duration = exoPlayer.duration.takeIf { it > 0 } ?: 1
            sliderPosition = (position.toFloat() / duration).coerceIn(0f, 1f)
            delay(500)
        }
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
            painter = rememberAsyncImagePainter(episode?.image ?: podcast.channelImage),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(bottom = 16.dp),
            contentScale = ContentScale.Crop
        )

        Text(podcast.trackName.toString(), fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFC533))
        Text(podcast.artistName.toString(), fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.Gray)

        episode?.title?.let {
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
                exoPlayer.seekTo((ratio * exoPlayer.duration).toLong())
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
            Text(formatTime(exoPlayer.duration), color = Color.Gray)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { /* Shuffle not handled */ }) {
                Icon(Icons.Default.Shuffle, contentDescription = "Shuffle", tint = Color.Gray)
            }

            IconButton(onClick = { /* Previous not handled */ }) {
                Icon(Icons.Default.FastRewind, contentDescription = "Previous")
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

            IconButton(onClick = { /* Next not handled */ }) {
                Icon(Icons.Default.FastForward, contentDescription = "Next")
            }

            IconButton(onClick = { /* Repeat not handled */ }) {
                Icon(Icons.Default.Repeat, contentDescription = "Repeat", tint = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text("LYRICS", color = Color.Gray, fontSize = 12.sp)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFFC533), shape = RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Text(
                text = episode?.description ?: "Không có lời mô tả.",
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
