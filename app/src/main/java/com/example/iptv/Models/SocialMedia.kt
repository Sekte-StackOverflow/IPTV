package com.example.iptv.Models

import com.google.gson.annotations.SerializedName

data class SocialMedia(
    @SerializedName("nama")
    val name: String,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("link")
    val url: String
)