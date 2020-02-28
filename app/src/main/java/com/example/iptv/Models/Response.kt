package com.example.iptv.Models

import com.google.gson.annotations.SerializedName

data class Response(
    val message: String,
    val status: String,
    @SerializedName("id")
    val data: String = "",
    @SerializedName("profile")
    val profile: String = ""
)