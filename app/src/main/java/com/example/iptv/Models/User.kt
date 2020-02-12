package com.example.iptv.Models

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("email")
    val email: String,

    @SerializedName("phone")
    val phone: String,

    @SerializedName("password")
    val password: String
) {
}