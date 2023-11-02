package com.example.medicapp_vip.view.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.medicapp_vip.R
import com.example.medicapp_vip.databinding.FragmentLoginBinding
import com.example.medicapp_vip.db.repository.PostEmailRepository

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var vm: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        vm = ViewModelProvider(this)[LoginViewModel::class.java]

        vm.result.observe(viewLifecycleOwner) {
            if (it){
                val bundle = Bundle()
                bundle.putString("email", binding.email.text.toString())
                val ieFragment = IdentificationEmailFragment()
                ieFragment.arguments = bundle

                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.login_main_view, ieFragment, "identificationEmail")
                    .addToBackStack("identificationEmail")
                    .commit()
            }
            else{
                Toast.makeText(context, "Ошибка, попробуйте позже", Toast.LENGTH_SHORT).show()
            }
        }
        setting()
        return binding.root
    }

    private fun setting() {
        binding.continueBtn.isEnabled = false
        binding.email.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.continueBtn.isEnabled = isEmailValid(p0)
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        binding.continueBtn.setOnClickListener{
            vm.postEmail(binding.email.text.toString())
        }
    }

    fun isEmailValid(email: CharSequence?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }


}

class LoginViewModel: ViewModel(){

    private val postEmailRepository = PostEmailRepository()

    val result = MutableLiveData<Boolean>()

    fun postEmail (email: String){
       result.value = postEmailRepository.request(email)
    }
}