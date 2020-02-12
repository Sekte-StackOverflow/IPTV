package com.example.iptv.api.service

import com.example.iptv.Models.Channel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ChannelApiService {

    @GET("film/json")
    fun getChannels(): Call<MutableList<Channel>>

    @GET("film/json/{id}")
    fun getOneChanner(@Path("id") id : String): Call<Channel>

}