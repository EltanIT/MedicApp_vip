package com.example.medicapp_vip.db.repository

import com.example.medicapp_vip.config.URLs
import com.example.medicapp_vip.objects.Order
import com.example.medicapp_vip.objects.Profile
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.lang.Exception
import java.util.concurrent.Executor

class PostOrderRepository {

    private val url = URLs().postOrderUrl
    private val mediaType = "application/json; charset=utf-8".toMediaType()

    fun request(order: Order, token: String): Boolean{
        val client = OkHttpClient()
        val gson = Gson()

        val dataMap = mapOf("clientAddress" to order.clientAddress,
        "date" to order.date,
        "patientsTakingTests" to order.patientsTakingTests,
        "phone" to order.phone,
        "price" to order.price,
        "comment" to order.comment)
        val data = gson.toJson(dataMap)

        val requestBody = data.toRequestBody(mediaType)

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .header("Authorization", token)
            .build()

        try {
            client.newCall(request).execute().use { response ->
                return response.isSuccessful
            }
        }catch (e: Exception){
            return false
        }

    }
}