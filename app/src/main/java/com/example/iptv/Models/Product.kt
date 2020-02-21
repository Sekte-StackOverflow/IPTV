package com.example.iptv.Models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Product(

    @SerializedName("id")
    val id: String?,
    @SerializedName("nama")
    val name: String?,
    @SerializedName("thumnail")
    val image: String? = "no_image",
    @SerializedName("detailgambar")
    val detail: String?,
    @SerializedName("youtube")
    val videoId: String?,
    val price: Int?,
    @SerializedName("discount")
    val diskon: Int = 0

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(image)
        parcel.writeString(detail)
        parcel.writeString(videoId)
        parcel.writeValue(price)
        parcel.writeInt(diskon)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }
}