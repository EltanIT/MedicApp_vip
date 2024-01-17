package com.example.medicapp_vip.objects
import com.google.gson.annotations.SerializedName

data class Analysis(
    @SerializedName("title") val title: String?=null,
    @SerializedName("description") val description: String?=null,
    @SerializedName("processDescription") val processDescription: String?=null,
    @SerializedName("deadline") val deadline: String?=null,
    @SerializedName("material") val material: String?=null,
    @SerializedName("price") val price: Int?=null,
    @SerializedName("currency") val currency: String?=null) {
}