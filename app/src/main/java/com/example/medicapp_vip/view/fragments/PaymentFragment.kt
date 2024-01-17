package com.example.medicapp_vip.view.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.medicapp_vip.R
import com.example.medicapp_vip.databinding.FragmentPaymentBinding
import com.example.medicapp_vip.db.repository.PostOrderRecordRepository
import com.example.medicapp_vip.db.repository.PostOrderRepository
import com.example.medicapp_vip.db.repositoryLocal.ClearShoppingCard
import com.example.medicapp_vip.db.repositoryLocal.GetOrder
import com.example.medicapp_vip.db.repositoryLocal.GetOrderRecord
import com.example.medicapp_vip.db.repositoryLocal.GetToken
import com.example.medicapp_vip.objects.News
import com.example.medicapp_vip.objects.Order
import com.example.medicapp_vip.view.activity.MainScreenActivity
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class PaymentFragment : Fragment(), MainScreenActivity.BackPressedListener {

    private lateinit var binding: FragmentPaymentBinding

    private lateinit var vm: PaymentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPaymentBinding.inflate(layoutInflater)
        vm = ViewModelProvider(this, PaymentViewModelFabric(requireContext()))[PaymentViewModel::class.java]
        setting()
        subscriptions()
        vm.setting()
        return binding.root
    }

    private fun setting() {
        binding.mainScreenBtn.setOnClickListener {
            val intent = Intent(requireActivity(), MainScreenActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        binding.backgroundView.setOnClickListener {}

        val animation = AnimationUtils.loadAnimation(context, R.anim.load_anim)
        binding.loadIc.startAnimation(animation)
    }

    private fun subscriptions() {
        vm.stateOrder.observe(viewLifecycleOwner){
            if (it){
                vm.clearShoppingCard()
                binding.loadView.visibility = GONE
                binding.loadCompleteView.visibility = VISIBLE

            }
            else{
//                Toast.makeText(context, "Ошибка подключения", Toast.LENGTH_SHORT).show()
//                requireActivity().supportFragmentManager.popBackStack()

                vm.clearShoppingCard()
                binding.loadView.visibility = GONE
                binding.loadCompleteView.visibility = VISIBLE
            }
        }
    }

    override fun onResume() {
        vm.postOrder()
        super.onResume()
    }

    override fun onBackPressed(): Boolean {
        return if (binding.loadCompleteView.isVisible){
            val intent = Intent(requireActivity(), MainScreenActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            true
        } else{
            false
        }
    }
}


class PaymentViewModel(val context: Context): ViewModel() {

    private val order = MutableLiveData<Order>()
    private val record = MutableLiveData<Uri>(Uri.parse(""))

    private val clearShoppingCard = ClearShoppingCard()
    private val postOrderRepository = PostOrderRepository()
    private val postOrderRecordRepository = PostOrderRecordRepository()
    private val getOrder = GetOrder()

    val stateOrder = MutableLiveData<Boolean>()

    private val coroutineScope = CoroutineScope(Dispatchers.IO)


    fun clearShoppingCard(){
        coroutineScope.launch {
            clearShoppingCard.request(context)
        }
    }

    private fun getOrder(){
        val body = getOrder.request(context)
        val gson = Gson()

        val type = object: TypeToken<Order>(){}.type
        order.value = gson.fromJson(body, type)
    }

    fun postOrder(){
            val data = postOrderRepository.request(order.value!!, GetToken().request(context)?:"")
            if (data!=null){
                val gson = Gson()
                val type = object : TypeToken<Order>(){}.type
                val gettingOrder: Order? = gson.fromJson(data, type)
                order.postValue(gettingOrder)
                stateOrder.postValue(postOrderRecordRepository.request(File(record.value?.path?:""),gettingOrder?.id?:""))
            }else{
                stateOrder.postValue(false)
            }
    }

    fun getOrderRecord(){
            record.value = (Uri.parse(GetOrderRecord().request(context) ?: ""))
    }

    fun setting() {
        getOrder()
        getOrderRecord()
        coroutineScope.launch {
            postOrder()
        }
    }
}

class PaymentViewModelFabric(val context: Context): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PaymentViewModel(context) as T
    }


}