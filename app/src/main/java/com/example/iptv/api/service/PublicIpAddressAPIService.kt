package com.example.iptv.api.service

import com.example.iptv.Models.PublicIPAddress
import retrofit2.Call
import retrofit2.http.GET

interface PublicIpAddressAPIService {
    @GET("?format=json")
    fun getPublicIp(): Call<PublicIPAddress>
}