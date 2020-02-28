package com.example.iptv.api.service

import com.example.iptv.Models.ApiResponse
import com.example.iptv.Models.AppDataSet
import com.example.iptv.Models.UserActivities
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface UserActivityService {
    @PUT("live/view")
    fun postActivity(@Body userActivities: UserActivities) : Call<ApiResponse>

    @PUT("shopping/view")
    fun postShopping(@Body userActivities: UserActivities) : Call<ApiResponse>

    @GET("api/app/json")
    fun getDataUtil(): Call<MutableList<AppDataSet>>
}