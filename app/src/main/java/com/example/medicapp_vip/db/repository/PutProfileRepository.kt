package com.example.medicapp_vip.db.repository

import com.example.medicapp_vip.config.URLs
import com.example.medicapp_vip.objects.Profile
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.lang.Exception
import java.util.concurrent.Executor

class PutProfileRepository {

    private val url = URLs().putProfileUrl
    private val mediaType = "application/json; charset=utf-8".toMediaType()

    fun request(profile: Profile?, token: String): Boolean{
        val client = OkHttpClient()
        val gson = Gson()

        val dataMap = mapOf(
            "firstName" to profile?.firstName,
            "lastName" to profile?.lastName,
            "patronymic" to profile?.patronymic,
            "dob" to profile?.dob,
            "gender" to profile?.gender)
        val data = gson.toJson(dataMap)

        val requestBody = data.toRequestBody(mediaType)

        val request = Request.Builder()
            .url(url)
            .put(requestBody)
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