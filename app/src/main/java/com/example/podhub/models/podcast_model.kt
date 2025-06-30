package com.example.podhub.models
import java.io.Serializable


data class PodcastResponseData(
    val collectionId: Long? = null,
    val artistId: Int? = null,
    val trackId: Long? = null,
    val artistName: String? = null,
    val collectionName: String? = null,
    val primaryGenreName: String? = null,
    val trackName: String? = null,
    val artworkUrl100: String? = null,
    val feedUrl: String? = null,
    val channelImage: String? = null,
    val episodes: List<Episode>? = null
) : Serializable

data class Episode(
    val title: String? = null,
    val description: String? = null,
    val audioUrl: String? = null,
    val image: String? = null
) : Serializable
