package com.example.podhub.RequestModel

import com.example.podhub.models.PodcastResponseData

data class LoginResponse(
    val message : Boolean?=null
)
data class HistoryResponse(
    val podCasts: List<PodcastResponseData>,
    val recentPlayed: List<Int>,
    val episodeIndex: List<Int>
)
data class SubtitleSegmentResponse(
    val startTime: String,
    val endTime: String,
    val text: String,
    val textVi: String? = null
)

data class ScriptResponse(
    val id: String,
    val podcastId: Int,
    val segments: List<SubtitleSegmentResponse>,
    val translateAudio: String? = null
)