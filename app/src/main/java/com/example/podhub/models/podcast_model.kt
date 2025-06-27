package com.example.podhub.models
import java.io.Serializable


data class PodcastResponseData(
    val collectionId: Long,
    val trackId: Long,
    val artistName: String,
    val collectionName: String,
    val primaryGenreName: String,
    val trackName: String,
    val artworkUrl100: String,
    val feedUrl: String,
    val channelImage: String,
    val episodes: List<Episode>
) : Serializable

data class Episode(
    val title: String,
    val description: String,
    val audioUrl: String,
    val image: String
) : Serializable
