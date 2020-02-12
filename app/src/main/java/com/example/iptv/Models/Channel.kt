package com.example.iptv.Models

import com.google.gson.annotations.SerializedName

data class Channel(
    val id: Int,
    val name: String,
    val views: Int = 0,
    @SerializedName("thumbnail")
    val imageUrl: String,
    @SerializedName("tipevideo")
    val status: String,
    @SerializedName("link")
    val videoUrl: String,
    @SerializedName("aksestipe")
    val userType: String
)