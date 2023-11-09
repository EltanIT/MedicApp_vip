package com.example.medicapp_vip.db.repositoryLocal

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class GetToken {

    private lateinit var sharedPreferences: SharedPreferences

    fun request(context: Context): String?{
        sharedPreferences = context.getSharedPreferences("Token", Context.MODE_PRIVATE)

        return sharedPreferences.getString("token", null)

    }

}