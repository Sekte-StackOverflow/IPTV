package com.example.iptv.api.service

import com.example.iptv.Models.Banner
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface BannerAPIService {
    @GET("api/bannerP/json")
    fun getBannerProduct(): Call<MutableList<Banner>>

    @GET("api/bannerC/json")
    fun getBannerChannel(): Call<MutableList<Banner>>

    @GET("api/bannerS/json")
    fun getBannerSosmed(): Call<MutableList<Banner>>

    @GET("api/bannerU/json")
    fun getImageDialog(): Call<MutableList<Banner>>
}