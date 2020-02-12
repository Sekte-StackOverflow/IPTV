package com.example.iptv.api.service

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface BannerAPIService {
    @GET("banner/json/{tipe}")
    fun getBanner(@Path("tipe") tipe: String): Call<MutableList<String>>
}