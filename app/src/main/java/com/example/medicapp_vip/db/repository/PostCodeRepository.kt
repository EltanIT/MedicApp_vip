package com.example.medicapp_vip.db.repository

import com.example.medicapp_vip.config.URLs
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.lang.Exception
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

class PostCodeRepository {

    private val url = URLs().postCodeUrl
    private val mediaType = "application/json; charset=utf-8".toMediaType()

    fun request(email: String, code: String): String?{
        val client = OkHttpClient().newBuilder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(3, TimeUnit.SECONDS)
            .readTimeout(3, TimeUnit.SECONDS)
            .build()
        val gson = Gson()

        val dataMap = mapOf<String, String>("email" to email,
            "recoveryCode" to code)
        val data = gson.toJson(dataMap)

        val requestBody = data.toRequestBody(mediaType)

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        try {
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful){
                    return response.body?.string()
                }
                return null

            }
        }catch (e: Exception){
            return null
        }

    }
}