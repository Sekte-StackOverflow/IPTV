package com.example.iptv.Models

import com.google.gson.annotations.SerializedName

class ApiResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("message")
    val message: String
)