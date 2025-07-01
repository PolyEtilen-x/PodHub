package com.example.podhub.Service


import com.example.podhub.data.PodcastResponse
import com.example.podhub.models.Artist
import com.example.podhub.models.PodcastResponseData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ArtistService {

    @GET("/artist")
    suspend fun getAllArtists(): Response<List<Artist>>
    @GET("/artist/{artistId}")
    suspend fun getPodCastByArtist(
        @Path("artistId") artistId: Int
    ): Response<List<PodcastResponseData>>
    @GET("/favourite/{uuid}")
    suspend fun getAllFavouriteArtist
                (@Path("uuid") uuid: String): Response<List<Artist>>

}
