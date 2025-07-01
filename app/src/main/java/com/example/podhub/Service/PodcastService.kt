package com.example.podhub.Service


import com.example.podhub.models.Episode
import com.example.podhub.models.PodcastResponseData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PodcastService {

    @GET("/podcast/category")
    suspend fun getPodcastsWithEpisodes(
        @Query("term") term: String,
        @Query("limit") limit: Int
    ): Response<List<PodcastResponseData>>

    @GET("/podcast/episodes")
    suspend fun getEpisodesByFeedUrl(
        @Query("feedUrl") feedUrl: String
    ): Response<List<Episode>>

}
