package com.example.iptv.Models
import com.google.gson.annotations.SerializedName

class UserReg(
    val email: String,
    val phone: String,
    val password: String,
    @SerializedName("tipe_user") // default android
    val tipeUser: String = "Android",
    @SerializedName("tipe_register") // default email or phone
    val tipeReg: String,
    @SerializedName("name")
    val name: String,
    val status: String = "1",
    val profile: String = "no_image.jpg"
)