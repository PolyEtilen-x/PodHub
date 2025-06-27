package com.example.podhub.models

//data class Podcast(
//    val id: String,
//    val title: String,
//    val author: String,
//    val imageUrl: String,
//    val audioUrl: String
//)

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
)

data class Episode(
    val title: String,
    val description: String,
    val audioUrl: String,
    val image: String
)