package com.example.medicapp_vip.view.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.constraintlayout.widget.R
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.medicapp_vip.config.Lists
import com.example.medicapp_vip.databinding.FragmentCreateCardBinding
import com.example.medicapp_vip.db.repository.PostProfileRepository
import com.example.medicapp_vip.db.repositoryLocal.GetToken
import com.example.medicapp_vip.objects.Profile
import com.example.medicapp_vip.view.activity.MainScreenActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateCardFragment : Fragment() {

    private lateinit var binding: FragmentCreateCardBinding
    private lateinit var vm: CreateCardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateCardBinding.inflate(layoutInflater)
        vm = ViewModelProvider(this, CreateCardViewModelFactory(requireContext()))[CreateCardViewModel::class.java]
        init()
        subscriptions()
        return binding.root
    }

    private fun subscriptions() {
        vm.result.observe(viewLifecycleOwner) {
            if (it){
                val intent = Intent(requireActivity(), MainScreenActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            else{
                Toast.makeText(context, "Ошибка, попробуйте позже", Toast.LENGTH_SHORT).show()
                //////
                val intent = Intent(requireActivity(), MainScreenActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                //////
            }
        }

        vm.profile.observe(viewLifecycleOwner){
            if (it!=null){
                binding.etName.setText(it.firstName)
                binding.etLastname.setText(it.lastName)
                binding.etMiddlename.setText(it.patronymic)
                binding.etBirthday.setText(it.dob)
                binding.floorS.setSelection(it.gender?.plus(1) ?: 0)
            }
        }
    }


    private fun init() {
        binding.etName.addTextChangedListener {
            binding.etName.isActivated = it.toString().isNotEmpty()
            vm.redactFirstName(it.toString())
        }
        binding.etMiddlename.addTextChangedListener {
            binding.etMiddlename.isActivated = it.toString().isNotEmpty()
            vm.redactPatronymic(it.toString())
        }
        binding.etLastname.addTextChangedListener {
            binding.etLastname.isActivated = it.toString().isNotEmpty()
            vm.redactLastName(it.toString())
        }
        binding.etBirthday.addTextChangedListener {
            binding.etBirthday.isActivated = it.toString().isNotEmpty()
            vm.redactDob(it.toString())
        }

        binding.continueBtn.setOnClickListener{
            vm.postCode()
        }
        binding.miss.setOnClickListener{
            val intent = Intent(requireActivity(), MainScreenActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        binding.floorS.onItemSelectedListener = object: OnItemSelectedListener,
            AdapterView.OnItemClickListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 != 0){
                    vm.redactGender(p2)
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {}
        }

        binding.floorS.adapter = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, Lists().genderList)
    }

}

class CreateCardViewModel(val context: Context): ViewModel() {
    val profile = MutableLiveData(Profile())

    private val postProfileRepository = PostProfileRepository()

    val result = MutableLiveData<Boolean>()

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    fun postCode (){
        coroutineScope.launch {
            result.postValue(postProfileRepository.request(profile.value!!,GetToken().request(context)?:""))
        }
    }

    fun redactFirstName(text: String){
        profile.value?.firstName = text
    }
    fun redactLastName(text: String){
        profile.value?.lastName = text
    }
    fun redactPatronymic(text: String){
        profile.value?.patronymic = text
    }
    fun redactDob(text: String){
        profile.value?.dob = text
    }
    fun redactGender(gender: Int){
        if (gender>0){
            profile.value?.gender = gender-1
        }
    }
}
class CreateCardViewModelFactory(val context: Context): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CreateCardViewModel(context) as T
    }
}

