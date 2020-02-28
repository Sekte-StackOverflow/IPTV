package com.example.iptv.Models

import com.google.gson.annotations.SerializedName

data class Channel(
    val id: Int,
    val name: String,
    @SerializedName("watching_a")
    var views1: Int = 0,
    @SerializedName("watching_w")
    val views2: Int = 0,
    @SerializedName("thumbnail")
    val imageUrl: String,
    @SerializedName("tipevideo")
    val status: String,
    @SerializedName("link")
    val videoUrl: String,
    @SerializedName("aksestipe")
    val userType: String
)