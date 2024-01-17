package com.example.medicapp_vip.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.medicapp_vip.R
import com.example.medicapp_vip.databinding.FragmentCreatePasswordBinding

class CreatePasswordFragment : Fragment() {

    private lateinit var binding: FragmentCreatePasswordBinding
    private var code = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreatePasswordBinding.inflate(layoutInflater)
        setting()
        return binding.root
    }

    private fun setting() {
        binding.missFragment.setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.login_main_view, CreateCardFragment(), "createCard")
                .addToBackStack("createCard")
                .commit()
        }


        binding.btn0.setOnClickListener{
            code+="0"
            redactPin()
        }
        binding.btn1.setOnClickListener{
            code+="1"
            redactPin()
        }
        binding.btn2.setOnClickListener{
            code+="2"
            redactPin()
        }
        binding.btn3.setOnClickListener{
            code+="3"
            redactPin()
        }
        binding.btn4.setOnClickListener{
            code+="4"
            redactPin()
        }
        binding.btn5.setOnClickListener{
            code+="5"
            redactPin()
        }
        binding.btn6.setOnClickListener{
            code+="6"
            redactPin()
        }
        binding.btn7.setOnClickListener{
            code+="7"
            redactPin()
        }
        binding.btn8.setOnClickListener{
            code+="8"
            redactPin()
        }
        binding.btn9.setOnClickListener{
            code+="9"
            redactPin()
        }
        binding.deleteButton.setOnClickListener{
            if (code.isNotEmpty()) {
                code = code.substring(0, code.length - 1)
                redactPinDelete()
            }
        }

    }

    private fun redactPinDelete() {
        if (binding.view1.isSelected) {
            if (binding.view2.isSelected) {
                if (binding.view3.isSelected) {
                    if (binding.view4.isSelected) {
                        binding.view4.isSelected = false
                    } else binding.view3.isSelected = false
                } else binding.view2.isSelected = false
            } else binding.view1.isSelected = false
        }
    }

    private fun redactPin(){
        if (binding.view1.isSelected) {
            if (binding.view2.isSelected) {
                if (binding.view3.isSelected) {
                    binding.view4.isSelected = true
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.login_main_view, CreateCardFragment())
                        .addToBackStack("createCard")
                        .commit()
                } else {
                    binding.view3.isSelected = true
                }
            } else {
                binding.view2.isSelected = true
            }
        } else {
            binding.view1.isSelected = true
        }
    }
}