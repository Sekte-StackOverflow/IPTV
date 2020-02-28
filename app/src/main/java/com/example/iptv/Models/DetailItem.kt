package com.example.iptv.Models

import com.google.gson.annotations.SerializedName

data class DetailItem(
    @SerializedName("deskripsi")
    val key: String,
    val value: String
) {
}