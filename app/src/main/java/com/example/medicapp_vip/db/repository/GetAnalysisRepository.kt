package com.example.medicapp_vip.db.repository

import com.example.medicapp_vip.config.URLs
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.lang.Exception
import java.util.concurrent.TimeUnit

class GetAnalysisRepository {

    private val url = URLs().getAnalysisUrl

    fun request(): String?{
        val client = OkHttpClient().newBuilder()
            .readTimeout(3, TimeUnit.SECONDS)
            .writeTimeout(3, TimeUnit.SECONDS)
            .connectTimeout(3, TimeUnit.SECONDS)
            .build()

        val request = Request.Builder()
            .url(url)
            .get()
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