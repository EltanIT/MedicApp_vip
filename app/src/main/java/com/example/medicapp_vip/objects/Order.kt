package com.example.medicapp_vip.objects

class Order(
    val clientAddress: Map<*,*>,
    val date: String?,
    val patientsTakingTests: ArrayList<Map<*,*>>,
    val phone: String,
    val price: Int,
    val comment: String
    ) {
}