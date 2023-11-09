package com.example.medicapp_vip.db.repositoryLocal

import android.content.Context
import android.content.SharedPreferences

class GetIconName {

    private lateinit var sharedPreferences: SharedPreferences

    fun request(context: Context): String?{
        sharedPreferences = context.getSharedPreferences("Profile", Context.MODE_PRIVATE)

//        val editor = sharedPreferences.edit()
//        editor.remove("card")
//        editor.apply()

        return sharedPreferences.getString("icon", null)
    }
}