package com.example.medicapp_vip.db.repositoryLocal

import android.content.Context
import android.content.SharedPreferences

class SaveShoppingCard {

    private lateinit var sharedPreferences: SharedPreferences

    fun request(context: Context, card: String): Boolean{
        sharedPreferences = context.getSharedPreferences("ShoppingCard", Context.MODE_PRIVATE)

        return try {
            val editor = sharedPreferences.edit()
            editor.remove("card")
            editor.putString("card", card)
            editor.apply()
            true
        }catch (e: Exception){
            false
        }

    }

}