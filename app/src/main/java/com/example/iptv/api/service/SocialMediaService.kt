package com.example.iptv.api.service

import com.example.iptv.Models.SocialMedia
import com.example.iptv.Models.SosMed
import retrofit2.Call
import retrofit2.http.GET

interface SocialMediaService {
    @GET("api/sosmed/json")
    fun getSocialMediaUrl(): Call<MutableList<SosMed>>

    @GET("api/sosmed/json")
    fun getAllSocial(): Call<MutableList<SocialMedia>>
}