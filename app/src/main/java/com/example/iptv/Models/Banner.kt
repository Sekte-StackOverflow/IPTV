package com.example.iptv.Models

import com.google.gson.annotations.SerializedName

data class Banner(
    @SerializedName("tipe")
    val type: String,
    @SerializedName("android")
    val img: String,
    @SerializedName("link")
    val link: String
)