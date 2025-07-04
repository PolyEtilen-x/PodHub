package com.example.podhub.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import com.example.podhub.R
import com.example.podhub.models.Artist
import kotlin.collections.forEach

@Composable
fun ArtistItem(
    name: String,
    imageUrl: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .wrapContentSize()
            .clickable(enabled = !isSelected) { onClick() }
            .alpha(if (isSelected) 0.5f else 1f)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(imageUrl)
                .crossfade(true)
                .placeholder(R.drawable.avatar_default)
                .error(R.drawable.avatar_default)
                .build(),
            contentDescription = "$name Avatar",
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Text(
            text = name,
            maxLines = 1,
            fontSize = 14.sp,
            overflow = TextOverflow.Ellipsis,
            color = Color.Black
        )
    }
}


@Composable
fun ArtistRow(artist: List<Artist>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .width(125.dp) // điều chỉnh chiều rộng mỗi cột artist
    ) {
        artist.forEach { artist ->
            ArtistItem(
                name = artist.name,
                imageUrl = artist.avatar,
                isSelected = false,
                onClick = {}
            )
        }

        // Nếu chưa đủ 2 dòng, thêm Spacer giữ bố cục
        repeat(2 - artist.size) {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}


