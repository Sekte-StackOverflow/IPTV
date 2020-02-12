package com.example.iptv.api.service

import com.example.iptv.Models.User
import retrofit2.Call
import retrofit2.http.*

interface UsersApiService {
    @GET("verifikasi/")
    fun getUsers(): Call<MutableList<User>>

    @GET("verifikasi/{email}")
    fun getUserViaEmail(@Path("email") email: String): Call<MutableList<User>>

    @GET("verifikasi/{phone}")
    fun getUserViaPhone(@Path("phone") phone: String): Call<MutableList<User>>

    @POST("verifikasi/")
    fun newUser(@Body user: User): Call<MutableList<User>>
    @PUT("verifikasi/{id}")
    fun  updateUser(@Path("id") id: String, @Body user: User): Call<User>
}