package com.example.podhub.ui.feature.home

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import com.example.podhub.RequestModel.HistoryRequest
import com.example.podhub.models.Episode
import com.example.podhub.viewmodels.HistoryViewModel
import com.example.podhub.viewmodels.PodcastViewModel
import com.example.podhub.viewmodels.ScriptViewModel
import kotlinx.coroutines.delay

@Composable
fun PlayerScreen(
    navController: NavHostController,
    podcastViewModel: PodcastViewModel,
    historyViewModel: HistoryViewModel,
    scriptViewModel: ScriptViewModel,
    initialEpisodeIndex: Int,
    recentPlay : Int,
    imageUrl: String,
    artistName: String,
    podCastId : Any
) {
    val context = LocalContext.current
    val episodes by podcastViewModel.episodes.collectAsState()

    // State for current episode index
    var currentEpisodeIndex by remember { mutableStateOf(initialEpisodeIndex) }

    // Get current episode safely
    val currentEpisode = episodes.getOrNull(currentEpisodeIndex)

    var isPlaying by remember { mutableStateOf(false) }
    var sliderPosition by remember { mutableStateOf(0f) }
    var currentPosition by remember { mutableStateOf(0L) }
    var totalDuration by remember { mutableStateOf(0L) }

    // Tab state for language selection
    var selectedTabIndex by remember { mutableStateOf(0) } // 0 = English, 1 = Vietnamese

    // --- ExoPlayer setup ---
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build()
    }

    suspend fun trackPlayback() {
        while (true) {
            currentPosition = exoPlayer.currentPosition
            totalDuration = if (exoPlayer.duration > 0) exoPlayer.duration else 0L
            sliderPosition = if (totalDuration > 0) {
                (currentPosition.toFloat() / totalDuration).coerceIn(0f, 1f)
            } else 0f
            delay(500)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            historyViewModel.setSelectedIndex(currentEpisodeIndex)
            historyViewModel.setRecentPlayed(exoPlayer.currentPosition)
            Log.d("tracker lastindex",currentEpisodeIndex.toString())
            Log.d("tracker recent",exoPlayer.currentPosition.toString())
            exoPlayer.release()
        }
    }

    LaunchedEffect(currentEpisodeIndex) {
        currentEpisode?.let { episode ->
            // Stop current playback
            exoPlayer.stop()
            exoPlayer.clearMediaItems()

            // Reset player state
            sliderPosition = 0f
            isPlaying = false

            // Load new media
            val mediaItem = MediaItem.fromUri(Uri.parse(episode.audioUrl))
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()

            if (currentEpisodeIndex == initialEpisodeIndex && recentPlay > 0) {
                exoPlayer.seekTo(recentPlay.toLong())
            }
            println(recentPlay)
            println(initialEpisodeIndex)

            exoPlayer.play()
            isPlaying = true
        }
    }

    // Start tracking immediately when component loads
    LaunchedEffect(Unit) {
        trackPlayback()
    }

    // Make the entire screen scrollable with LazyColumn
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header with back button
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(
                    onClick = {
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("dismissHistoryDialog", true)
                        navController.popBackStack()
                    }
                ) {
                    Icon(
                        Icons.Default.ExpandMore,
                        contentDescription = "Collapse",
                        tint = Color(0xFFFFC533)
                    )
                }
            }
        }

        // Album artwork
        item {
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(bottom = 16.dp),
                contentScale = ContentScale.Crop
            )
        }

        // Podcast title
        item {
            Text(
                text = currentEpisode?.title ?: "Unknown Podcast",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFC533)
            )
        }

        // Artist name
        item {
            Text(
                text = artistName ?: "Unknown Artist",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
        }

        // Episode title
        item {
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
        }

        // Spacer
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Progress slider
        item {
            Slider(
                value = sliderPosition,
                onValueChange = { ratio ->
                    if (totalDuration > 0) {
                        val seekPosition = (ratio * totalDuration).toLong()
                        exoPlayer.seekTo(seekPosition)
                        sliderPosition = ratio
                        currentPosition = seekPosition
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFFFFC533),
                    activeTrackColor = Color(0xFFFFC533)
                )
            )
        }

        // Time display
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(formatTime(currentPosition), color = Color.Gray)
                Text(formatTime(totalDuration), color = Color.Gray)
            }
        }

        // Spacer
        item {
            Spacer(modifier = Modifier.height(32.dp))
        }

        // Control buttons
        item {
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
        }

        // Spacer
        item {
            Spacer(modifier = Modifier.height(32.dp))
        }

        // Language Tab Bar
        item {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.fillMaxWidth(),
                containerColor = Color.Transparent,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = Color(0xFFFFC533)
                    )
                }
            ) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = {
                        // Pause player when switching tabs
                        if (exoPlayer.isPlaying) {
                            exoPlayer.pause()
                            isPlaying = false
                        }
                        selectedTabIndex = 0
                    },
                    text = {
                        Text(
                            "English",
                            color = if (selectedTabIndex == 0) Color(0xFFFFC533) else Color.Gray
                        )
                    }
                )
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = {
                        // Pause player when switching tabs
                        if (exoPlayer.isPlaying) {
                            exoPlayer.pause()
                            isPlaying = false
                        }
                        selectedTabIndex = 1
                    },
                    text = {
                        Text(
                            "Tiếng Việt",
                            color = if (selectedTabIndex == 1) Color(0xFFFFC533) else Color.Gray
                        )
                    }
                )
            }
        }

        // Script Sync Section
        item {
            ScriptSyncSection(
                scriptViewModel = scriptViewModel,
                exoPlayer = exoPlayer,
                showVi = selectedTabIndex == 1,
                podCastId = podCastId // You may need to adjust this based on your Episode model
            )
        }

        // Description header
        item {
            Text("DESCRIPTION", color = Color.Gray, fontSize = 12.sp)
        }

        // Description content
        item {
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
}

fun formatTime(ms: Long): String {
    val totalSec = ms / 1000
    val min = totalSec / 60
    val sec = totalSec % 60
    return "%d:%02d".format(min, sec)
}

@Composable
fun ScriptSyncSection(
    scriptViewModel: ScriptViewModel,
    exoPlayer: ExoPlayer,
    showVi: Boolean,
    podCastId: Any
) {
    val context = LocalContext.current
    val scriptState by scriptViewModel.script.collectAsState()
    val podcastId = scriptState?.podcastId
    val segments = scriptState?.segments.orEmpty()
    val scriptEng = remember(segments) { segments.map { it.text } }
    val scriptViet = remember(segments) { segments.map { it.textVi.orEmpty() } }
    val audioviet = scriptState?.translateAudio

    var highlightedIndex by remember { mutableStateOf(0) }
    val listState = rememberLazyListState()

    // Vietnamese audio player state
    var isVietnameseAudioPlaying by remember { mutableStateOf(false) }
    var vietnameseAudioPosition by remember { mutableStateOf(0f) }

    // Create separate ExoPlayer for Vietnamese audio
    val vietnamesePlayer = remember {
        ExoPlayer.Builder(context).build()
    }

    // Track Vietnamese audio playback
    suspend fun trackVietnamesePlayback() {
        while (!vietnamesePlayer.isPlaying) {
            delay(100)
        }
        while (vietnamesePlayer.isPlaying) {
            val position = vietnamesePlayer.currentPosition
            val duration = vietnamesePlayer.duration.takeIf { it > 0 } ?: 1
            vietnameseAudioPosition = (position.toFloat() / duration).coerceIn(0f, 1f)
            delay(500)
        }
    }

    // Setup Vietnamese audio when available
    LaunchedEffect(audioviet) {
        audioviet?.let { audioUrl ->
            val mediaItem = MediaItem.fromUri(Uri.parse(audioUrl))
            vietnamesePlayer.setMediaItem(mediaItem)
            vietnamesePlayer.prepare()
        }
    }

    // Start Vietnamese audio tracking immediately
    LaunchedEffect(Unit) {
        trackVietnamesePlayback()
    }

    // Dispose Vietnamese player
    DisposableEffect(Unit) {
        onDispose {
            vietnamesePlayer.release()
        }
    }

    LaunchedEffect(segments, exoPlayer) {
        while (true) {
            val positionMs = exoPlayer.currentPosition
            val foundIndex = segments.indexOfLast { seg ->
                val startMs = parseTimestampToMs(seg.startTime)
                val endMs = parseTimestampToMs(seg.endTime)
                positionMs in startMs until endMs
            }
            if (foundIndex != highlightedIndex && foundIndex >= 0) {
                highlightedIndex = foundIndex
            }
            delay(100)
        }
    }

    LaunchedEffect(highlightedIndex) {
        if (highlightedIndex > 0) {
            listState.animateScrollToItem(highlightedIndex)
        }
    }

    val scriptList = if (showVi) scriptViet else scriptEng

    if (podcastId.toString() != podCastId.toString()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(120.dp)
                .background(Color(0xFFF4F4F4), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Hiện chưa có bản dịch",
                color = Color.Gray,
                fontSize = 16.sp
            )
        }
    } else {
        Column {
            // Vietnamese Audio Controls (only show when Vietnamese tab is selected and audio is available)
            if (showVi && audioviet != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFC533)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🇻🇳 Bản dịch tiếng Việt",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )

                            IconButton(
                                onClick = {
                                    if (vietnamesePlayer.isPlaying) {
                                        vietnamesePlayer.pause()
                                        isVietnameseAudioPlaying = false


                                    } else {
                                        if (exoPlayer.isPlaying) {
                                            exoPlayer.pause()
                                        }
                                        vietnamesePlayer.play()
                                        isVietnameseAudioPlaying = true
                                    }
                                },
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Color.White, RoundedCornerShape(20.dp))
                            ) {
                                Icon(
                                    imageVector = if (isVietnameseAudioPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = if (isVietnameseAudioPlaying) "Pause Vietnamese" else "Play Vietnamese",
                                    tint = Color(0xFFFFC533),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Vietnamese audio progress slider
                        Slider(
                            value = vietnameseAudioPosition,
                            onValueChange = { ratio ->
                                val seekPosition = (ratio * vietnamesePlayer.duration).toLong()
                                vietnamesePlayer.seekTo(seekPosition)
                                vietnameseAudioPosition = ratio
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = SliderDefaults.colors(
                                thumbColor = Color.White,
                                activeTrackColor = Color.White,
                                inactiveTrackColor = Color.White.copy(alpha = 0.3f)
                            )
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = formatTime(vietnamesePlayer.currentPosition),
                                color = Color.White,
                                fontSize = 12.sp
                            )
                            Text(
                                text = formatTime(if (vietnamesePlayer.duration > 0) vietnamesePlayer.duration else 0),
                                color = Color.White,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

            // Script content
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .heightIn(min = 120.dp, max = 300.dp)
                    .background(Color(0xFFF4F4F4), RoundedCornerShape(12.dp))
            ) {
                itemsIndexed(scriptList) { index, line ->
                    Text(
                        text = line,
                        color = if (index == highlightedIndex) Color(0xFFFFC533) else Color.Gray,
                        fontSize = if (index == highlightedIndex) 20.sp else 16.sp,
                        fontWeight = if (index == highlightedIndex) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier
                            .padding(vertical = 4.dp, horizontal = 8.dp)
                            .background(
                                color = if (index == highlightedIndex) Color(0xFFFBE9C6) else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}

// Utility for timestamp parsing
fun parseTimestampToMs(str: String?): Long {
    if (str.isNullOrBlank()) return 0L
    val parts = str.split(".")
    val sec = parts.getOrNull(0)?.toLongOrNull() ?: 0L
    val ms = when (parts.getOrNull(1)?.length ?: 0) {
        1 -> (parts[1].toIntOrNull() ?: 0) * 100
        2 -> (parts[1].toIntOrNull() ?: 0) * 10
        3 -> (parts[1].toIntOrNull() ?: 0)
        else -> 0
    }
    return sec * 1000 + ms
}