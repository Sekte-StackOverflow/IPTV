package com.example.iptv.api.service

import com.example.iptv.Models.Product
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApiService {
    @GET("produk/json")
    fun getProducts(): Call<MutableList<Product>>

    @GET("produk/json/{id}")
    fun getOneProduct(@Path("id") id : String): Call<Product>
}