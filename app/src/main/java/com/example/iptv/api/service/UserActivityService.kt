package com.example.iptv.api.service

import com.example.iptv.Models.ApiResponse
import com.example.iptv.Models.UserActivities
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserActivityService {
    @POST("aktivitas")
    fun postActivity(@Body userActivities: UserActivities) : Call<ApiResponse>
}