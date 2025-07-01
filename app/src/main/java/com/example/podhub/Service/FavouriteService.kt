package com.example.podhub.Service

import com.example.podhub.RequestModel.FavouriteRequest
import com.example.podhub.RequestModel.LoginResponse
import com.example.podhub.data.PodcastResponse
import com.example.podhub.models.Artist
import com.example.podhub.models.PodcastResponseData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
interface FavouriteService {

    @POST("favourite/collect/{uuid}")
    suspend fun collectFavourite(
        @Path("uuid") uuid: String,
        @Body dto: FavouriteRequest
    ): Response<LoginResponse>

    @GET("favourite/suggest/{uuid}")
    suspend fun getSuggestedPodcasts(
        @Path("uuid") uuid: String
    ): Response<List<PodcastResponseData>>

    @POST("favourite/podcast/{uuid}/{trackId}")
    suspend fun togglePodcastFavourite(
        @Path("uuid") uuid: String,
        @Path("trackId") trackId: Long
    ): Response<LoginResponse>

    @GET("favourite/check/{uuid}/{trackId}")
    suspend fun isPodcastFavourited(
        @Path("uuid") uuid: String,
        @Path("trackId") trackId: Long
    ): Response<Boolean>
}
