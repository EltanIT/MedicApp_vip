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
import com.example.medicapp_vip.db.repositoryLocal.SaveToken
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class IdentificationEmailFragment : Fragment() {

    private lateinit var binding: FragmentIdentificationEmailBinding
    private var email = ""
    private lateinit var vm: IdentificationEmailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIdentificationEmailBinding.inflate(layoutInflater)
        vm = ViewModelProvider(this, IdentificationEmailViewModelFabric(requireContext()))[IdentificationEmailViewModel::class.java]
        val bundle = arguments
        if (bundle != null){
            email = bundle.getString("email", null)
        }
        setting()
        subscriptions()
        return binding.root
    }

    private fun subscriptions() {
        vm.result.observe(viewLifecycleOwner) {
            if (it){
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.login_main_view, CreatePasswordFragment())
                    .commit()
            }
            else{
                Toast.makeText(context, "Ошибка, попробуйте позже", Toast.LENGTH_SHORT).show()
                //////
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.login_main_view, CreatePasswordFragment())
                    .commit()
                //////
            }
        }

    }

    private fun setting() {
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

class IdentificationEmailViewModel(val context: Context): ViewModel(){

    private val postCodeRepository = PostCodeRepository()
    private val saveToken = SaveToken()

    val result = MutableLiveData<Boolean>()


    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    fun postCode (email: String, code: String){
        coroutineScope.launch {
            val body = postCodeRepository.request(email, code)

            if (body != null){
                val objectMapper = ObjectMapper()
                val stringMap: java.util.HashMap<*, *>? = objectMapper.readValue(
                    body,
                    HashMap::class.java
                )
                val text:String = stringMap?.get("token") as String
                val token = text.substring(text.indexOf(".") + 1, text.lastIndexOf("."))
                saveToken(token)
            }
            else{
                result.postValue(false)
            }
        }
    }

    fun saveToken(token: String){
        result.postValue(saveToken.request(context, token))
    }

}
class IdentificationEmailViewModelFabric(_context: Context): ViewModelProvider.Factory{
    val context = _context
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return IdentificationEmailViewModel(context = context) as T
    }


}