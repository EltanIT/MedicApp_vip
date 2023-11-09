package com.example.medicapp_vip.db.repositoryLocal

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class DontSaveAddress {

    private lateinit var sharedPreferences: SharedPreferences

    fun request(context: Context): Boolean{
        sharedPreferences = context.getSharedPreferences("Order", Context.MODE_PRIVATE)

        return try {
            val editor = sharedPreferences.edit()
            editor.remove("address")
            editor.apply()
            true
        }catch (e: Exception){
            Log.i("address", e.toString())
            false
        }

    }

}