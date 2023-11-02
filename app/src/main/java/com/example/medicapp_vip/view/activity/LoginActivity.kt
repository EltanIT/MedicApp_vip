package com.example.medicapp_vip.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.medicapp_vip.R
import com.example.medicapp_vip.view.fragments.LoginFragment

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        selectFragment()
    }

    private fun selectFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.login_main_view, LoginFragment())
            .commit()
    }
}