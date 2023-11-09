package com.example.medicapp_vip.db.repositoryLocal

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class SaveIconName {

    private lateinit var sharedPreferences: SharedPreferences

    fun request(context: Context, name: String): Boolean{
        sharedPreferences = context.getSharedPreferences("Profile", Context.MODE_PRIVATE)

        return try {
            val editor = sharedPreferences.edit()
            editor.remove("icon")
            editor.putString("icon", name)
            editor.apply()
            true
        }catch (e: Exception){
            Log.i("Profile", e.toString())
            false
        }

    }

}