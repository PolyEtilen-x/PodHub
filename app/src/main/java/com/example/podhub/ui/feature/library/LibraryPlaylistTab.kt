package com.example.podhub.ui.feature.library

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.podhub.R
import com.example.podhub.data.PodcastResponse
import com.example.podhub.models.Playlist
import com.example.podhub.models.PodcastResponseData
import com.example.podhub.utils.CloudinaryUploader
import com.example.podhub.viewmodels.SharedPlaylistViewModel


@Composable
fun LibraryPlaylistsTab(navController: NavHostController, sharedPlaylistViewModel: SharedPlaylistViewModel) {
    val playlists = remember {
        mutableStateListOf(
            Playlist("grainy days", 10, null),
            Playlist("raindrops", 10, null)
        )
    }

    var showDialog by remember { mutableStateOf(false) }
    val viewModel: SharedPlaylistViewModel = viewModel()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDialog = true }
        ) {
            Surface(
                shape = CircleShape,
                color = Color(0xFFFFC533),
                shadowElevation = 4.dp,
                modifier = Modifier.size(56.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "+",
                        color = Color.White,
                        fontSize = 28.sp
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Tạo playlist mới",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFC533)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Playlist của bạn",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFFC533)
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(playlists) { playlist ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            sharedPlaylistViewModel.selectedPlaylist = playlist
                            navController.navigate("playlist_detail")
                        }

                ) {
                    val painter = rememberAsyncImagePainter(
                        model = playlist.imageUrl ?: R.drawable.folder
                    )
                    Image(
                        painter = painter,
                        contentDescription = "Playlist Icon",
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = playlist.title,
                            fontSize = 16.sp,
                            color = Color(0xFFFFC533),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${playlist.podcastCount} podcasts",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }

    if (showDialog) {
        AddPlaylistDialog(
            onDismiss = { showDialog = false },
            onAdd = { title, selectedCount, imageUrl, podcasts ->
                playlists.add(Playlist(title, selectedCount, imageUrl, podcasts))
                showDialog = false
            }
        )
    }
}

@Composable
fun AddPlaylistDialog(
    onDismiss: () -> Unit,
    onAdd: (String, Int, String?, List<PodcastResponseData>) -> Unit
) {
    var playlistName by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var uploadedImageUrl by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val allPodcasts = PodcastResponse.podcastList.orEmpty()

    var query by remember { mutableStateOf("") }
    val filteredPodcasts = remember(query) {
        if (query.isBlank()) allPodcasts
        else allPodcasts.filter {
            it.trackName?.contains(query, ignoreCase = true) == true ||
                    it.artistName?.contains(query, ignoreCase = true) == true
        }
    }

    val selectedPodcasts = remember { mutableStateMapOf<String, PodcastResponseData>() }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            imageUri = it
            CloudinaryUploader.uploadImage(
                context = context,
                imageUri = it,
                playlistName = playlistName,
                onSuccess = { url ->
                    uploadedImageUrl = url
                    Log.d("UPLOAD_SUCCESS", url)
                },
                onError = { err ->
                    Log.e("UPLOAD_FAIL", err)
                }
            )
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onAdd(
                    playlistName,
                    selectedPodcasts.size,
                    uploadedImageUrl,
                    selectedPodcasts.values.toList()
                )
            }) {
                Text("Thêm", fontWeight = FontWeight.Bold, color = Color(0xFFFFC533))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy", fontWeight = FontWeight.Bold, color = Color(0xFFFF0000))
            }
        },
        title = {
            Text(
                "Tạo Playlist Mới",
                color = Color(0xFFFFC533),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        },
        containerColor = Color(0xFFFAF6F5),

        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.background(Color(0xFFFAF6F5))
            ) {
                OutlinedTextField(
                    value = playlistName,
                    onValueChange = { playlistName = it },
                    label = {
                        Text(
                            text = "Tên Playlist",
                            color = Color(0xFFFFC533),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    },
                    singleLine = true
                )

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF5F5F5),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clickable { launcher.launch("image/*") }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = if (imageUri != null) "Đang tải ảnh..." else "Chọn ảnh đại diện playlist",
                            color = Color.Gray
                        )
                    }
                }

                Divider()

                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text("Tìm kiếm podcast...") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    filteredPodcasts.take(10).forEach { podcast ->
                        val checked = selectedPodcasts.contains(podcast.trackId.toString())
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = checked,
                                onCheckedChange = {
                                    if (it) selectedPodcasts[podcast.trackId.toString()] = podcast
                                    else selectedPodcasts.remove(podcast.trackId.toString())
                                }
                            )
                            Column {
                                Text(
                                    podcast.trackName ?: "No name",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    podcast.artistName ?: "Unknown",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}
