package com.example.medicapp_vip.view.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.medicapp_vip.R
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

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var vm: ProfileViewModel

    private val GALLERY_CONST = 300

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
        vm.getToken()
        vm.getIconName()
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
            vm.saveProfile(
                binding.etName.text.toString(),
                binding.etLastname.text.toString(),
                binding.etMiddlename.text.toString(),
                binding.etBirthday.text.toString(),
                binding.floorS.selectedItemPosition)
        }

        val genders = ArrayList<String>()
        genders.add("Пол")
        genders.add("Мужской")
        genders.add("Женский")

        binding.floorS.adapter = ArrayAdapter(requireContext(), androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item, genders)

    }

    private fun subscriptions() {
        vm.profile.observe(viewLifecycleOwner){
            binding.etName.setText(it.firstName)
            binding.etLastname.setText(it.lastName)
            binding.etMiddlename.setText(it.patronymic)
            binding.etBirthday.setText(it.dob)
            it.gender?.let { it1 -> binding.floorS.setSelection(it1) }
        }

        vm.resultRequest.observe(viewLifecycleOwner){
            if (!it){
                Toast.makeText(context, "Ошибка подключения", Toast.LENGTH_SHORT).show()
            }

        }
        vm.icon.observe(viewLifecycleOwner){
            binding.icon.setImageURI(it)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GALLERY_CONST && data != null){
            val uri = data.data
            if (uri != null){
                vm.saveIcon(uri)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}


class ProfileViewModel(val context: Context): ViewModel() {

    val profile = MutableLiveData<Profile>()
    val icon = MutableLiveData<Uri>()
    private lateinit var file: File
    private var token: String? = ""
    private var iconName: String? = ""

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
           val body = getProfile.request(token!!)
            if (body != null){
                val gson = Gson()
                val type = object : TypeToken<Profile>(){}.type
                profile.postValue(gson.fromJson(body, type))
            }
        }
    }

    fun saveProfile(name: String, lastname: String, patronymic: String, birthday: String, gender: Int){
        coroutine.launch {
            profile.postValue(Profile(name, lastname, patronymic, birthday, gender))
            resultRequest.postValue(putProfile.request(profile.value, token!!))
            postIcon.request(file, token!!)
        }
    }

    fun getToken(){
        token = getToken.request(context)
    }

    fun saveIcon(data: Uri){
        coroutine.launch {
            icon.postValue(data)
            file = File(data.path)
            iconName = file.name
            saveIconName()
        }

    }
    fun getIcon(){
        coroutine.launch {
            if (iconName!= null){
                val body = getIcon.request(iconName!!)
            }
        }
    }

    fun getIconName(){
        coroutine.launch {
            iconName = getIconName.request(context)
        }
    }

    fun saveIconName(){
        coroutine.launch {
            saveIconName.request(context, iconName.toString())
        }
    }

}

class ProfileViewModelFabric(_context: Context): ViewModelProvider.Factory{
    val context = _context
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileViewModel(context = context) as T
    }


}