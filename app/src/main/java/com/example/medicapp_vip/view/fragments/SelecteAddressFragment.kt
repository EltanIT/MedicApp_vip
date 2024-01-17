package com.example.medicapp_vip.view.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.medicapp_vip.R
import com.example.medicapp_vip.databinding.FragmentSelecteAddressBinding
import com.example.medicapp_vip.db.repositoryLocal.DontSaveAddress
import com.example.medicapp_vip.db.repositoryLocal.GetAddress
import com.example.medicapp_vip.db.repositoryLocal.GetShoppingCard
import com.example.medicapp_vip.db.repositoryLocal.SaveAddress
import com.example.medicapp_vip.db.repositoryLocal.SaveShoppingCard
import com.example.medicapp_vip.objects.Address
import com.example.medicapp_vip.objects.Analysis
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SelecteAddressFragment(val listener: PlaceOnOrderFragment.AddressListener) : Fragment() {

    private lateinit var binding: FragmentSelecteAddressBinding
    private lateinit var vm: SelectedAddressViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelecteAddressBinding.inflate(layoutInflater)
        vm = ViewModelProvider(this, SelectedAddressViewModelFabric(requireContext()))[SelectedAddressViewModel::class.java]
        subscriptions()
        setting()
        return binding.root
    }

    private fun setting() {
        binding.addressContinueButton.setOnClickListener {
            if (binding.saveSwitch.isChecked){
                val address = Address(binding.addressEt.text.toString(),
                binding.longitudeEt.text.toString().toInt(),
                binding.widthEt.text.toString().toInt(),
                binding.heightEt.text.toString().toInt(),
                binding.flatEt.text.toString().toInt(),
                binding.entranceEt.text.toString().toInt(),
                binding.floorEt.text.toString().toInt(),
                binding.intercomEt.text.toString(),
                binding.placeLiveEt.text.toString())
                listener.execute(address)
                closeView()
            }
            else{
                closeView()
            }
        }

        binding.saveSwitch.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                binding.buildingTypeEt.visibility = VISIBLE
            }else{
                binding.buildingTypeEt.visibility = GONE
            }
        }

        binding.closeAddressView.setOnClickListener {
            closeView()
        }
        binding.backgroundView.setOnClickListener {
            closeView()
        }
        binding.addressCardView.setOnClickListener {

        }
    }

    override fun onResume() {
        vm.getAddress()
        openView()
        super.onResume()
    }

    private fun subscriptions() {
        vm.address.observe(viewLifecycleOwner){
            if (it != null){
                binding.addressEt.setText(it.address)
                binding.flatEt.setText(it.flat.toString())
                binding.entranceEt.setText(it.entrance.toString())
                binding.floorEt.setText(it.floor.toString())
                binding.heightEt.setText(it.height.toString())
                binding.widthEt.setText(it.width.toString())
                binding.longitudeEt.setText(it.longitude.toString())
                binding.placeLiveEt.setText(it.buildingType)
                binding.intercomEt.setText(it.intercon)
                binding.saveSwitch.isChecked = true
            }

        }
    }


    private fun openView(){
        val animation = AnimationUtils.loadAnimation(context,R.anim.open_view)
        binding.addressCardView.startAnimation(animation)
    }

    private fun closeView(){
        val animation = AnimationUtils.loadAnimation(context,R.anim.close_view)
        animation.setAnimationListener(object: Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                requireActivity().supportFragmentManager.popBackStack()
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }

        })
        binding.addressCardView.startAnimation(animation)
    }
}


class SelectedAddressViewModel(val context: Context): ViewModel() {
    val address = MutableLiveData<Address>()

    private val getAddress = GetAddress()

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    fun getAddress(){
        coroutineScope.launch {
            val body = getAddress.request(context = context)
            val gson = Gson()
            if (body!= null){
                address.postValue(gson.fromJson(body, Address::class.java))
            }
            else{
                address.postValue(null)

            }
            Log.i("address", address.value.toString())
        }
    }

}

class SelectedAddressViewModelFabric(val context: Context): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SelectedAddressViewModel(context = context) as T
    }
}