package com.example.medicapp_vip.db.repository

import com.example.medicapp_vip.config.URLs
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.lang.Exception
import java.net.URL

class GetOrderOnIdRepository {

    private val url = URLs().getOrderOnIdUrl

    fun request(id: String): String?{
        val client = OkHttpClient()
        val url1 = URL(url.toString()+id)

        val request = Request.Builder()
            .url(url1)
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