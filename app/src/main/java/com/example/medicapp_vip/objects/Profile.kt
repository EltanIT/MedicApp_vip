package com.example.medicapp_vip.objects
import com.google.gson.annotations.SerializedName

data class Profile(
    @SerializedName("firstName") var firstName: String? = null,
    @SerializedName("lastName") var lastName: String? = null,
    @SerializedName("patronymic") var patronymic: String? = null,
    @SerializedName("dob") var dob: String? = null,
    @SerializedName("gender") var gender: Int? = null) {
}