package com.example.podhub.Service


import com.example.podhub.RequestModel.ScriptResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ScriptService {
    @GET("script/podcast/{podcastId}")
    suspend fun getScriptByPodcastId(
        @Path("podcastId") podcastId: Int
    ): ScriptResponse
}