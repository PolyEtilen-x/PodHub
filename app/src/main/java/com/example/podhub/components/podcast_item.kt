package com.example.podhub.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.podhub.R
import com.example.podhub.models.Podcast

@Composable
fun PodcastItem(
    podcast: Podcast,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(60.dp),
        color = Color(0xFFFFC533),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(podcast.imageUrl)
                    .crossfade(true)
                    .placeholder(com.example.podhub.R.drawable.avatar_default)
                    .error(R.drawable.avatar_default)
                    .build(),
                contentDescription = "Podcast Image",
                modifier = Modifier
                    .fillMaxHeight()
                    .width(60.dp)
                    .clip(RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp)),
                contentScale = ContentScale.Crop,
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = podcast.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun PodcastRow(podcasts: List<Podcast>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .width(180.dp)
    ) {
        podcasts.forEach { podcast ->
            PodcastItem(
                podcast = podcast,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // if podcast/2 la so le thi them 1 item
        repeat(2 - podcasts.size) {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}





