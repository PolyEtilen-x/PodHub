package com.example.podhub.Service


import com.example.podhub.models.Artist
import retrofit2.Response
import retrofit2.http.GET

interface ArtistService {

    @GET("/artist")
    suspend fun getAllArtists(): Response<List<Artist>>
}
