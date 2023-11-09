package com.example.medicapp_vip.db.repositoryLocal

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class SaveOrder {

    private lateinit var sharedPreferences: SharedPreferences

    fun request(context: Context, order: String): Boolean{
        sharedPreferences = context.getSharedPreferences("Order", Context.MODE_PRIVATE)

        return try {
            val editor = sharedPreferences.edit()
            editor.remove("order")
            editor.putString("order", order)
            editor.apply()
            true
        }catch (e: Exception){
            Log.i("Order", e.toString())
            false
        }

    }

}