package com.example.iptv.api.service

import com.example.iptv.Models.ApiResponse
import com.example.iptv.Models.User
import com.example.iptv.Models.UserReg
import retrofit2.Call
import retrofit2.http.*

interface UsersApiService {

    @FormUrlEncoded
    @POST("api/login")
    fun authentication(@Field("email") email: String, @Field("password") password: String): Call<ApiResponse>

    @POST("api/register")
    fun newUser(@Body user: UserReg): Call<ApiResponse>

    @PUT("api/user/{id}")
    fun updateUser(@Field("id") id: String, @Body user: UserReg): Call<ApiResponse>
}