package com.example.medicapp_vip.db.repository

import com.example.medicapp_vip.config.URLs
import okhttp3.OkHttpClient
import okhttp3.Request

class GetProfileRepository {

    private val url = URLs().getProfileUrl
    fun request(token: String): String?{
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url)
            .get()
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