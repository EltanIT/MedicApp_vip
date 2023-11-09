package com.example.medicapp_vip.db.repositoryLocal

import android.content.Context
import android.content.SharedPreferences
import com.example.medicapp_vip.objects.Analysis
import com.google.gson.Gson

class AddItemInShoppingCard {

    private lateinit var sharedPreferences: SharedPreferences

    fun request(context: Context, analysis: Analysis): Boolean{
        sharedPreferences = context.getSharedPreferences("ShoppingCard", Context.MODE_PRIVATE)

        return try {
            val gson = Gson()
            val cardList: ArrayList<Analysis> = gson.fromJson(sharedPreferences.getString("card", null), ArrayList<Analysis>()::class.java)
            cardList.add(analysis)
            val editor = sharedPreferences.edit()
            editor.remove("card")
            editor.putString("card", gson.toJson(cardList))
            editor.apply()
            true
        }catch (e: Exception){
            false
        }

    }

}