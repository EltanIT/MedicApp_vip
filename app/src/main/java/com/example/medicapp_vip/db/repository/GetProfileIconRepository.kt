package com.example.medicapp_vip.db.repository

import com.example.medicapp_vip.config.URLs
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.lang.Exception

class GetProfileIconRepository {

    private val url = URLs().getProfileUrl

    fun request(name: String): String?{
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url)
            .header("filename", name)
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