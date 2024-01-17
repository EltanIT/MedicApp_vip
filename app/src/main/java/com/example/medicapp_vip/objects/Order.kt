package com.example.medicapp_vip.objects
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class Order(
    @SerializedName("id") var id: String? = null,
    @SerializedName("clientAddress") var clientAddress: Address? = null,
    @SerializedName("date") var date: LocalDateTime? = null,
    @SerializedName("patientsTakingTests") var patientsTakingTests: ArrayList<Profile>? = null,
    @SerializedName("phone") var phone: String? = null,
    @SerializedName("price") var price: Int? = 0,
    @SerializedName("comment") var comment: String? = null) {
}