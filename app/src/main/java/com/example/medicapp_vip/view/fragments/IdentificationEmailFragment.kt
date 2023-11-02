package com.example.medicapp_vip.view.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.medicapp_vip.R
import com.example.medicapp_vip.databinding.FragmentIdentificationEmailBinding
import com.example.medicapp_vip.db.repository.PostCodeRepository
import com.example.medicapp_vip.db.repository.PostEmailRepository

class IdentificationEmailFragment : Fragment() {

    private lateinit var binding: FragmentIdentificationEmailBinding
    private var email = ""
    private lateinit var vm: IdentificationEmailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIdentificationEmailBinding.inflate(layoutInflater)
        vm = ViewModelProvider(this)[IdentificationEmailViewModel::class.java]
        val bundle = arguments
        if (bundle != null){
            email = bundle.getString("email", null)
        }
        setting()
        return binding.root
    }

    private fun setting() {
        vm.result.observe(viewLifecycleOwner) {
            if (it){
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.login_main_view, CreatePasswordFragment())
                    .commit()
            }
            else{
                Toast.makeText(context, "Ошибка, попробуйте позже", Toast.LENGTH_SHORT).show()
            }
        }

        binding.code.requestFocus()
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput( binding.code, InputMethodManager.SHOW_IMPLICIT)


        binding.code.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().length == 4){
                    vm.postCode(email, p0.toString())
                }
            }

        })
    }

}

class IdentificationEmailViewModel: ViewModel(){

    private val postCodeRepository = PostCodeRepository()

    val result = MutableLiveData<Boolean>()

    fun postCode (email: String, code: String){
        result.value = postCodeRepository.request(email, code)
    }
}