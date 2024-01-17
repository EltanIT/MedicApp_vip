package com.example.medicapp_vip.objects
import com.google.gson.annotations.SerializedName

data class News(
    @SerializedName("id") val id: String?=null,
    @SerializedName("title") val title: String?=null,
    @SerializedName("description") val description: String?=null,
    @SerializedName("price") val price: Int?=null,
    @SerializedName("image") val image: String?=null)