package com.example.podhub.Service

import com.example.podhub.RequestModel.HistoryRequest
import com.example.podhub.RequestModel.HistoryResponse
import com.example.podhub.RequestModel.LoginResponse
import retrofit2.Response
import retrofit2.http.*

interface HistoryService {

    @POST("history/{uuid}")
    suspend fun postHistory(
        @Path("uuid") uuid: String,
        @Body request: HistoryRequest
    ): Response<LoginResponse>

    @GET("history/{uuid}")
    suspend fun getHistory(
        @Path("uuid") uuid: String
    ): Response<HistoryResponse>
}
