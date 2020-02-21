package com.example.iptv.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIClient {
    private lateinit var retrofit: Retrofit
    private var BASE_URL: String = "https://eyeplus.co.id/admin-eyeplus/"
    private var PUBLIC_IP_URL: String = "https://api.ipify.org/"

    companion object {
        val IMAGE_PATH = "https://eyeplus.co.id/admin-eyeplus/media/img/"
    }

    fun getIPPublicInstance(): Retrofit {
        retrofit = Retrofit.Builder()
            .baseUrl(PUBLIC_IP_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit
    }

    fun getAPI(): Retrofit {
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit
    }

}