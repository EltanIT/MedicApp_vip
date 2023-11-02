package com.example.medicapp_vip.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.medicapp_vip.databinding.FragmentHello1Binding
import com.example.medicapp_vip.view.activity.LoginActivity

class Hello1Fragment : Fragment() {

   private lateinit var binding: FragmentHello1Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHello1Binding.inflate(layoutInflater)
        setting()
        return binding.root
    }

    private fun setting() {
        binding.missText.setOnClickListener{
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

}