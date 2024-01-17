package com.example.medicapp_vip.view.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.medicapp_vip.R
import com.example.medicapp_vip.config.Lists
import com.example.medicapp_vip.databinding.FragmentProfileBinding
import com.example.medicapp_vip.db.repository.GetProfileIconRepository
import com.example.medicapp_vip.db.repository.GetProfileRepository
import com.example.medicapp_vip.db.repository.PostOrderRepository
import com.example.medicapp_vip.db.repository.PostProfileIconRepository
import com.example.medicapp_vip.db.repository.PostProfileRepository
import com.example.medicapp_vip.db.repository.PutProfileRepository
import com.example.medicapp_vip.db.repositoryLocal.ClearShoppingCard
import com.example.medicapp_vip.db.repositoryLocal.GetIconName
import com.example.medicapp_vip.db.repositoryLocal.GetOrder
import com.example.medicapp_vip.db.repositoryLocal.GetToken
import com.example.medicapp_vip.db.repositoryLocal.SaveIconName
import com.example.medicapp_vip.objects.Order
import com.example.medicapp_vip.objects.Profile
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.File
import java.net.URI

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var vm: ProfileViewModel

    private val GALLERY_CONST = 100

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        vm = ViewModelProvider(this, ProfileViewModelFabric(requireContext()))[ProfileViewModel::class.java]
        setting()
        subscriptions()
        return binding.root
    }

    override fun onResume() {
        vm.getProfile()
        super.onResume()
    }

    private fun setting() {
        binding.iconClick.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, GALLERY_CONST)
        }

        binding.save.setOnClickListener {
            vm.saveProfile()
        }

        binding.etName.addTextChangedListener {
            vm.redactFirstName(it.toString())
        }
        binding.etMiddlename.addTextChangedListener {
            vm.redactMiddlename(it.toString())
        }
        binding.etLastname.addTextChangedListener {
            vm.redactLastname(it.toString())
        }
        binding.etBirthday.addTextChangedListener {
            vm.redactDob(it.toString())
        }
        binding.floorS.onItemSelectedListener = object : OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                vm.redactFloor(p2)
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        binding.floorS.adapter = ArrayAdapter(requireContext(), androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item, Lists().genderList)

    }

    private fun subscriptions() {
        vm.profile.observe(viewLifecycleOwner){
            if (it!=null){
                binding.etName.setText(it.firstName)
                binding.etLastname.setText(it.lastName)
                binding.etMiddlename.setText(it.patronymic)
                binding.etBirthday.setText(it.dob)
                binding.floorS.setSelection(it.gender?.plus(1) ?: 0)
            }
        }

        vm.resultRequest.observe(viewLifecycleOwner){
            if (!it){
                Toast.makeText(context, "Ошибка подключения", Toast.LENGTH_SHORT).show()
            }
        }

        vm.icon.observe(viewLifecycleOwner){
            if (it!=null){
//                val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, it)
                binding.icon.setImageURI(it)
            }else{
                Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GALLERY_CONST && data != null){
            val uri = data.data
            if (uri != null){
                vm.saveIcon(uri)
            }else{
                Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}

class ProfileViewModel(val context: Context): ViewModel() {

    val profile = MutableLiveData<Profile>()
    val icon = MutableLiveData<Uri>()

    val resultRequest = MutableLiveData<Boolean>()

    private val coroutine = CoroutineScope(Dispatchers.IO)

    private val putProfile = PutProfileRepository()
    private val postIcon = PostProfileIconRepository()
    private val getIcon = GetProfileIconRepository()
    private val getProfile = GetProfileRepository()

    private val getIconName = GetIconName()
    private val saveIconName = SaveIconName()

    private val getToken = GetToken()

    fun getProfile(){
        coroutine.launch {
            val body = getProfile.request(getToken.request(context)?:"")
            if (body != null){
                val gson = Gson()
                val type = object : TypeToken<Profile>(){}.type
                profile.postValue(gson.fromJson(body, type))
            }
        }
        getIcon()
    }

    fun saveProfile(){
        coroutine.launch {
            resultRequest.postValue(putProfile.request(profile.value, getToken.request(context)?:""))
            postIcon.request(File(icon.value?.path?:""), getToken.request(context)?:"")
        }
    }

    fun saveIcon(data: Uri){
        coroutine.launch {
            icon.postValue(data)
            Log.i("profile", data.toString())
            val file = File(data.path)
            saveIconName(file.name)
        }
    }
    fun getIcon(){
        coroutine.launch {
            val body = getIcon.request(getIconName.request(context)?:"")
        }
    }

    fun saveIconName(name: String){
        coroutine.launch {
            saveIconName.request(context, name)
        }
    }

    fun redactFirstName(text: String) {
        profile.value?.firstName = text
    }
    fun redactFloor(gender: Int) {
        if (gender>0){
            profile.value?.gender = gender-1
        }
    }
    fun redactMiddlename(toString: String) {
        profile.value?.patronymic = toString
    }
    fun redactLastname(toString: String) {
        profile.value?.lastName = toString
    }
    fun redactDob(toString: String) {
        profile.value?.dob = toString
    }
}

class ProfileViewModelFabric(_context: Context): ViewModelProvider.Factory{
    val context = _context
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileViewModel(context = context) as T
    }


}