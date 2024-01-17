package com.example.medicapp_vip.db.repository

import com.example.medicapp_vip.config.URLs
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.File
import java.net.URL

class PostOrderRecordRepository {

    private val urlText = URLs().postOrderRecordUrl

    fun request(file: File, id: String): Boolean{
        val client = OkHttpClient()
        val url = URL(urlText+id)

        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("file", file.name,
                RequestBody.create("image/*".toMediaTypeOrNull(), file))
            .build()

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
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