package com.example.iptv.api.service

import com.example.iptv.Models.Banner
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface BannerAPIService {
    @GET("bannerP/json")
    fun getBannerProduct(): Call<MutableList<Banner>>

    @GET("bannerC/json")
    fun getBannerChannel(): Call<MutableList<Banner>>
}