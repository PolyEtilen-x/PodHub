package com.example.podhub.Retrofit

import com.example.podhub.Service.ArtistService
import com.example.podhub.Service.PodcastService
import com.example.podhub.Service.UserService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.getValue
import kotlin.jvm.java



object RetrofitInstance {
//    private const val SECOND_URL = "http://192.168.0.100:3000"
    private const val SECOND_URL = "http://192.168.139.1:3000"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(SECOND_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val userService: UserService by lazy {
        retrofit.create(UserService::class.java)
    }
    val artistService: ArtistService by lazy {
        retrofit.create(ArtistService::class.java)
    }
    val podcastService: PodcastService by lazy {
        retrofit.create(PodcastService::class.java)
    }


}

