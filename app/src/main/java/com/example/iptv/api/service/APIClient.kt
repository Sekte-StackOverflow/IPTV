package com.example.iptv.api.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class APIClient {
    private lateinit var retrofit: Retrofit
    private var BASE_URL: String = ""
    private var PUBLIC_IP_URL: String = "https://api.ipify.org/"
    private var IP_GEOLOCATION: String = "http://ip-api.com/json/"

    fun getIPPublicInstance(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(PUBLIC_IP_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit
    }

    fun getIpGeolacationInstance(): Retrofit {
        retrofit = Retrofit.Builder()
            .baseUrl(IP_GEOLOCATION)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit
    }
}