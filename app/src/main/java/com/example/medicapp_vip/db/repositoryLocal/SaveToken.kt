package com.example.medicapp_vip.db.repositoryLocal

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class SaveToken {

    private lateinit var sharedPreferences: SharedPreferences

    fun request(context: Context, token: String): Boolean{
        sharedPreferences = context.getSharedPreferences("Token", Context.MODE_PRIVATE)

        return try {
            val editor = sharedPreferences.edit()
            editor.remove("token")
            editor.putString("token", token)
            editor.apply()
            true
        }catch (e: Exception){
            Log.i("Token", e.toString())
            false
        }

    }

}