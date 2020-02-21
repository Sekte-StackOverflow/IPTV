package com.example.iptv.Models
import com.google.gson.annotations.SerializedName

class UserReg(
    val email: String,
    val phone: String,
    val password: String,
    @SerializedName("tipe_user") // default android
    val tipeUser: String,
    @SerializedName("tipe_register") // default email or phone
    val tipeReg: String,
    @SerializedName("name")
    val name: String
)