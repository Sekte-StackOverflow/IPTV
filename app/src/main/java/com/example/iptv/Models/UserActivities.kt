package com.example.iptv.Models

import com.google.gson.annotations.SerializedName

data class UserActivities(
    @SerializedName("id")
    val idMovie: String,
    @SerializedName("activity")
    val activity: String,
    @SerializedName("iduser")
    val userId: String
)