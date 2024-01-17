package com.example.medicapp_vip.view.fragments

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.medicapp_vip.R
import com.example.medicapp_vip.databinding.FragmentIdentificationEmailBinding
import com.example.medicapp_vip.db.repository.PostCodeRepository
import com.example.medicapp_vip.db.repository.PostEmailRepository
import com.example.medicapp_vip.db.repositoryLocal.SaveToken
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class IdentificationEmailFragment : Fragment() {

    private lateinit var binding: FragmentIdentificationEmailBinding
    private lateinit var vm: IdentificationEmailViewModel

    private lateinit var timer: CountDownTimer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIdentificationEmailBinding.inflate(layoutInflater)
        vm = ViewModelProvider(this, IdentificationEmailViewModelFabric(requireContext()))[IdentificationEmailViewModel::class.java]
        val bundle = arguments
        vm.setting(bundle)
        setting()
        subscriptions()
        return binding.root
    }

    private fun subscriptions() {
        vm.statePostCode.observe(viewLifecycleOwner) {
            if (it == true){
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.login_main_view, CreatePasswordFragment(), "createPassword")
                    .addToBackStack("createPassword")
                    .commit()
            }
            else if(it == false){
                Toast.makeText(context, "Ошибка, попробуйте позже", Toast.LENGTH_SHORT).show()
                binding.back.isEnabled = true
                binding.code.isEnabled = true
                //////
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.login_main_view, CreatePasswordFragment())
                    .commit()
                //////

            }
        }

        vm.statePostEmail.observe(viewLifecycleOwner) {
            if (it){
                timer.start()
            }
            else{
                Toast.makeText(context, "Ошибка, попробуйте позже", Toast.LENGTH_SHORT).show()
                requireActivity().supportFragmentManager.popBackStack()
            }
        }

    }

    private fun setting() {
        binding.code.requestFocus()
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput( binding.code, InputMethodManager.SHOW_IMPLICIT)

        binding.code.addTextChangedListener {
            if (it.toString().length == 4){
                binding.back.isEnabled = false
                binding.code.isEnabled = false
                vm.postCode(it.toString())
            }
        }

        binding.back.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.timerText.setOnClickListener {
            vm.getCode()
        }

        timer = object : CountDownTimer(60000, 1000) {
            override fun onTick(p0: Long) {
                binding.timerText.isEnabled = false
                binding.timerText.text = "Отправить код повторно можно будет через ${(p0/1000)} секунд"
            }

            override fun onFinish() {
                binding.timerText.isEnabled = true
                binding.timerText.text = "Отправить код повторно"
            }

        }
        timer.start()
    }
}

class IdentificationEmailViewModel(val context: Context): ViewModel(){
    private var email = ""

    private val postCodeRepository = PostCodeRepository()
    private val postEmailRepository = PostEmailRepository()
    private val saveToken = SaveToken()

    val statePostCode = MutableLiveData<Boolean?>(null)
    val statePostEmail = MutableLiveData<Boolean>()

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    fun postCode (code: String){
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
                Log.i("token", token)
                saveToken(token)
            }
            else{
                statePostCode.postValue(false)
            }
        }
    }

    fun getCode(){
        coroutineScope.launch {
            statePostEmail.postValue(postEmailRepository.request(email))
        }
    }

    fun saveToken(token: String){
        statePostCode.postValue(saveToken.request(context, token))
    }

    fun setting(bundle: Bundle?) {
        if (bundle != null){
            email = bundle.getString("email", null)
        }
    }

}
class IdentificationEmailViewModelFabric(_context: Context): ViewModelProvider.Factory{
    val context = _context
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return IdentificationEmailViewModel(context = context) as T
    }


}