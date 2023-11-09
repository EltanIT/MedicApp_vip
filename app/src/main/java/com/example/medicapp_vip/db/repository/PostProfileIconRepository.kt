package com.example.medicapp_vip.db.repository

import com.example.medicapp_vip.config.URLs
import com.example.medicapp_vip.objects.Profile
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.lang.Exception
import java.util.concurrent.Executor

class PostProfileIconRepository {

    private val url = URLs().postProfileImageUrl

    fun request(file: File, token: String): Boolean{
        val client = OkHttpClient()

        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("file", file.name,
                RequestBody.create("image/*".toMediaTypeOrNull(), file))
            .build()

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