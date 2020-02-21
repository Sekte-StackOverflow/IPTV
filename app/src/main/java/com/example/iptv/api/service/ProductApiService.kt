package com.example.iptv.api.service

import com.example.iptv.Models.DetailItem
import com.example.iptv.Models.Product
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApiService {
    @GET("api/produk/json")
    fun getProducts(): Call<MutableList<Product>>

    @GET("api/produk/jsonL/{id}")
    fun getProductDetail(@Path("id") id : String): Call<MutableList<DetailItem>>

}