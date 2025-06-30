package com.example.podhub.Service

import com.example.podhub.RequestModel.Login
import com.example.podhub.RequestModel.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST("users/login")
    suspend fun loginUser(@Body LoginRequest: Login): Response<LoginResponse>
}
