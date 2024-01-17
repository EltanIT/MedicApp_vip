package com.example.medicapp_vip.db.repository

import com.example.medicapp_vip.config.URLs
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.lang.Exception

class UpdateTokenRepository {

    private val url = URLs().putProfileUrl
    private val mediaType = "application/json; charset=utf-8".toMediaType()

    fun request(token: String): String?{
        val client = OkHttpClient()
        val gson = Gson()

        val dataMap = mapOf("token" to token)
        val data = gson.toJson(dataMap)

        val requestBody = data.toRequestBody(mediaType)

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        try {
            client.newCall(request).execute().use { response ->
                return response.body?.string()
            }
        }catch (e: Exception){
            return null
        }

    }
}