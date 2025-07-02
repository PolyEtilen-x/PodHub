package com.example.podhub.RequestModel

data class Login(
    val uuid : String
)
data class FavouriteRequest(
    val artists: List<Long> = emptyList(),
    val category: List<Long> = emptyList()
)
data class HistoryRequest(
    val podcastId: Long,
    val recentPlay: Long ,
    val episodeIndex: Int
)
