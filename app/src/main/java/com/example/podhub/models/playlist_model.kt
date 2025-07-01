package com.example.podhub.models

import com.example.podhub.models.PodcastResponseData
import java.io.Serializable

data class Playlist(
    val title: String,
    val podcastCount: Int,
    val imageUrl: String?,
    val podcasts: List<PodcastResponseData> = emptyList()
) : Serializable