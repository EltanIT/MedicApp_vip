package com.example.medicapp_vip.db.repository

import com.example.medicapp_vip.config.URLs
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.lang.Exception
import java.net.URL

class GetOrdersRepository {

    private val url = URLs().getOrdersUrl

    fun request(token: String): String?{
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url)
            .header("Authorization", token)
            .build()

        try {
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful){
                    return response.body?.string()
                }
            }
        }catch (e: Exception){
            return null
        }
        return null

    }
}