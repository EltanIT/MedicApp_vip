package com.example.medicapp_vip.view.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.constraintlayout.widget.R
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.medicapp_vip.databinding.FragmentCreateCardBinding
import com.example.medicapp_vip.db.repository.PostCodeRepository
import com.example.medicapp_vip.db.repository.PostProfileRepository
import com.example.medicapp_vip.objects.Profile
import com.example.medicapp_vip.view.activity.LoginActivity
import com.example.medicapp_vip.view.activity.MainScreenActivity

class CreateCardFragment : Fragment() {

    private lateinit var binding: FragmentCreateCardBinding
    private var floor = 0
    private val floorList = ArrayList<String>()

    private lateinit var vm: CreateCardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateCardBinding.inflate(layoutInflater)
        vm = ViewModelProvider(this)[CreateCardViewModel::class.java]
        addFloor()
        setting()
        return binding.root
    }

    private fun addFloor(){
        floorList.add("Пол")
        floorList.add("Мужской")
        floorList.add("Женский")
    }

    private fun setting() {
        vm.result.observe(viewLifecycleOwner) {
            if (it){
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(com.example.medicapp_vip.R.id.login_main_view, CreatePasswordFragment())
                    .commit()
            }
            else{
                Toast.makeText(context, "Ошибка, попробуйте позже", Toast.LENGTH_SHORT).show()
            }
        }

        binding.etName.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                binding.etName.isActivated = p0.toString().isNotEmpty()
            }

        })
        binding.etMiddlename.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                binding.etMiddlename.isActivated = p0.toString().isNotEmpty()
            }

        })
        binding.etLastname.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                binding.etLastname.isActivated = p0.toString().isNotEmpty()
            }

        })
        binding.etBirthday.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                binding.etBirthday.isActivated = p0.toString().isNotEmpty()
            }

        })

        binding.continueBtn.setOnClickListener{
            val intent = Intent(requireActivity(), MainScreenActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
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
                    floor = p2
                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                TODO("Not yet implemented")
            }

        }


        binding.floorS.adapter = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, floorList)
    }

}

class CreateCardViewModel: ViewModel(){

    private val postProfileRepository = PostProfileRepository()

    val result = MutableLiveData<Boolean>()

    fun postCode (name: String, lastName: String, patronymic: String, dob: String, gender: Int){
        result.value = postProfileRepository.request(Profile(name, lastName, patronymic, dob, gender))
    }
}

