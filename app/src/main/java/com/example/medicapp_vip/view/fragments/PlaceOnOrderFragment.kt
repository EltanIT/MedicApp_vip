package com.example.medicapp_vip.view.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.medicapp_vip.R
import com.example.medicapp_vip.databinding.FragmentPlaceOnOrderBinding
import com.example.medicapp_vip.db.repositoryLocal.GetAddress
import com.example.medicapp_vip.db.repositoryLocal.GetShoppingCard
import com.example.medicapp_vip.db.repositoryLocal.SaveAddress
import com.example.medicapp_vip.db.repositoryLocal.SaveOrder
import com.example.medicapp_vip.db.repositoryLocal.SaveOrderRecord
import com.example.medicapp_vip.objects.Address
import com.example.medicapp_vip.objects.Analysis
import com.example.medicapp_vip.objects.Order
import com.example.medicapp_vip.objects.Profile
import com.example.medicapp_vip.view.adapter.OrderProfilesAdapter
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class PlaceOnOrderFragment : Fragment() {

    interface DateListener{
       fun execute(date: LocalDateTime)
    }
    interface AddressListener{
        fun execute(address: Address)
    }
    interface AddPatientListener{
        fun execute(profile: Profile)
    }
    interface OrderProfilesListeners{
        fun deleteItem(profile: Profile)
        fun checkAnalysis(analysis: Analysis, check: Boolean)
    }

    private lateinit var dateListener: DateListener
    private lateinit var addressListener: AddressListener
    private lateinit var addPatientListener: AddPatientListener
    private lateinit var orderProfilesListeners: OrderProfilesListeners

    private var orderProfilesAdapter: OrderProfilesAdapter? = null

    private lateinit var binding: FragmentPlaceOnOrderBinding
    private lateinit var vm: PlaceOnOrderViewModel
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaceOnOrderBinding.inflate(layoutInflater)
        vm = ViewModelProvider(this, PlaceOnOrderViewModelFabric(requireContext()))[PlaceOnOrderViewModel::class.java]
        setting()
        interfaces()
        subscriptions()
        return binding.root
    }

    private fun interfaces() {
        dateListener = object: DateListener{
            @RequiresApi(Build.VERSION_CODES.O)
            @SuppressLint("SetTextI18n")
            override fun execute(date: LocalDateTime) {
                binding.dateTextView.text = "Сегодня, ${date.dayOfMonth} ${date.month} ${date.hour} ${date.minute}"
                vm.saveDate(date)
            }
        }

        addressListener = object : AddressListener {
            override fun execute(address: Address) {
                vm.saveAddress(address)
                binding.addressTextView.text = "${address.address}, кв. ${address.flat}"
                binding.addressTextView.setTextColor(resources.getColor(R.color.black))
            }
        }

        addPatientListener = object : AddPatientListener {
            override fun execute(profile: Profile) {
                orderProfilesAdapter?.add(profile)
                vm.addPatient(profile)
                val count = binding.countAnalysisView.text.toString().toInt()+vm.shoppingCard.value?.size!!
                binding.countAnalysisView.text = (count).toString()
                binding.analysisCountDeclantion.text = getWordsDeclension(count)
                val price = binding.priceView.text.toString().toInt()
                binding.priceView.text = (price + (vm.priceShoppingCard)).toString()
            }
        }

        orderProfilesListeners = object : OrderProfilesListeners {
            override fun deleteItem(profile: Profile) {
                vm.deletePatient(profile)
            }

            override fun checkAnalysis(analysis: Analysis, check: Boolean) {
                vm.redactAnalysis(analysis, check)
                if (check){
                    val count = binding.countAnalysisView.text.toString().toInt()+1
                    binding.countAnalysisView.text = (count).toString()
                    binding.analysisCountDeclantion.text = getWordsDeclension(count)
                    val price = binding.priceView.text.toString().toInt()
                    binding.priceView.text = (price + (analysis.price?:0)).toString()
                }else{
                    val count = binding.countAnalysisView.text.toString().toInt()-1
                    binding.countAnalysisView.text = (count).toString()
                    binding.analysisCountDeclantion.text = getWordsDeclension(count)
                    val price = binding.priceView.text.toString().toInt()
                    binding.priceView.text = (price - (analysis.price?:0)).toString()
                }
            }
        }

    }

    private fun setting() {
        binding.continuePlaceOrderButton.isEnabled = false

        binding.addressLl.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .add(R.id.main_screen_all_view, SelecteAddressFragment(addressListener))
                .addToBackStack("selectedAddress")
                .commit()
        }

        binding.dateLl.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .add(R.id.main_screen_all_view, SelectedDateFragment(dateListener))
                .addToBackStack("selectedDate")
                .commit()
        }

        binding.addPatientButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .add(R.id.main_screen_all_view, AddPatientFragment(addPatientListener))
                .addToBackStack("addPatient")
                .commit()
        }

        binding.backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.continuePlaceOrderButton.setOnClickListener {
            vm.saveOrder()
//            requireActivity().supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_screen_all_view, PaymentFragment())
                .addToBackStack("payment")
                .commit()
        }

        binding.phoneNumber.addTextChangedListener {
            vm.savePhone(it.toString())
        }

        binding.commentsEt.addTextChangedListener {
            vm.saveComment(it.toString())
        }

        binding.redord.setOnClickListener {
            val intent = Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION)
            startActivityForResult(intent, 100)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 100 && data != null){
            val uri = data.data
            vm.saveRecord(uri ?: Uri.parse(""))
            Toast.makeText(requireContext(), "Запись ${uri.toString()} сохранена", Toast.LENGTH_SHORT).show()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun subscriptions() {
        vm.order.observe(viewLifecycleOwner){
            if (it != null){
                if (it.date!=null){
                    binding.dateTextView.text = "Сегодня, ${it.date?.dayOfMonth} ${it.date?.month} ${it.date?.hour} ${it.date?.minute}"
                }
                binding.phoneNumber.setText(it.phone)
                binding.commentsEt.setText(it.comment)
                binding.priceView.text = it.price.toString()
                if (it.patientsTakingTests?.size != null){
                    val countAnalysis = vm.shoppingCard.value?.size?.times(it.patientsTakingTests?.size?:0)
                    binding.countAnalysisView.text = "$countAnalysis"
                    binding.analysisCountDeclantion.text = getWordsDeclension(countAnalysis?:0)
                }
                if (it.clientAddress != null){
                    binding.addressTextView.text = "${it.clientAddress?.address}, кв. ${it.clientAddress?.flat}"
                    binding.addressTextView.setTextColor(resources.getColor(R.color.black))
                }
                if (vm.shoppingCard.value != null && orderProfilesAdapter == null){
                    orderProfilesAdapter = OrderProfilesAdapter(vm.shoppingCard.value!!, it.patientsTakingTests!!, orderProfilesListeners)
                    binding.selectedPatientList.adapter = orderProfilesAdapter
                }
            }
        }

        vm.checkEnable.observe(viewLifecycleOwner){
            binding.continuePlaceOrderButton.isEnabled = it
        }
    }

    private fun getWordsDeclension(countAnalysis: Int): String {
        val preLastDigit: Int = countAnalysis % 100 / 10

        return if (preLastDigit == 1) {
            " Анализов"
        } else when (countAnalysis % 10) {
            1 -> " Анализ"
            2, 3, 4 -> " Анализа"
            else -> " Анализов"
        }
    }

    override fun onResume() {
        vm.setting()
        super.onResume()
    }
}

class PlaceOnOrderViewModel(val context: Context): ViewModel() {

    val shoppingCard = MutableLiveData<ArrayList<Analysis>>()
    val order = MutableLiveData(Order())
    val checkEnable = MutableLiveData<Boolean>()
    private val record = MutableLiveData<Uri>()
    var priceShoppingCard = 0

    private val getShoppingCard = GetShoppingCard()
    private val getAddress = GetAddress()
    private val saveAddress = SaveAddress()
    private val saveOrder = SaveOrder()
    private val saveOrderRecord = SaveOrderRecord()

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private fun checkEnable(){
        coroutineScope.launch {
                checkEnable.postValue((order.value?.clientAddress!= null
                        && order.value?.date !=null
                        && order.value?.phone?.length==10
                        && order.value?.patientsTakingTests?.size != null
                        && order.value?.patientsTakingTests?.size != 0))
        }
    }

    fun savePhone(number: String){
        order.value?.phone = number
        checkEnable()

    }
    fun saveComment(com: String){
        order.value?.comment = com
    }

    fun addPatient(profile: Profile){
        order.value?.patientsTakingTests?.add(profile)
        order.value?.price = order.value?.price?.plus(priceShoppingCard)
    }
    fun deletePatient(profile: Profile){
        order.value?.patientsTakingTests?.removeIf {
            it == profile
        }
        var price = 0
        for(item in shoppingCard.value!!){
            price += item.price?:0
        }
        order.value?.price = order.value?.price?.minus(price)
        order.value = order.value
    }

    fun saveOrder() {
        val objectMapper = ObjectMapper()
        val analysisList = ArrayList<Map<*, *>>()
        for (i in shoppingCard.value!!) {
            analysisList.add(objectMapper.convertValue(i, object : TypeReference<Map<*, *>>() {}))
        }
        coroutineScope.launch {
            val gson = Gson()
            saveOrder.request(context, gson.toJson(order.value))
        }
    }

    fun getShoppingCard(){
            val body = getShoppingCard.request(context = context)
            val gson = Gson()
            order.value?.patientsTakingTests = ArrayList()
            order.value?.patientsTakingTests?.add(Profile("Имя", "Фамилия", "fdasa", "f", 0))
            if (body!= null){
                val type = object: TypeToken<ArrayList<Analysis>>() {}.type
                val list = gson.fromJson<ArrayList<Analysis>>(body, type)
                for( i in list){
                    priceShoppingCard += i.price!!
                }
                order.value?.price = priceShoppingCard
                shoppingCard.postValue(gson.fromJson(body, type))
            }
            else{
                val list = ArrayList<Analysis>()
                shoppingCard.value = list
                Log.i("shoppingCard", shoppingCard.value.toString())
            }
    }
    fun getAddress(){
            val body = getAddress.request(context = context)
            val gson = Gson()
            if (body!= null){
                val type = object: TypeToken<Address>(){}.type
                order.value?.clientAddress = gson.fromJson(body, type)
            }
            else{
                order.value?.clientAddress = null
            }
            Log.i("address", order.value?.clientAddress.toString())
            checkEnable()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun saveDate(ldt: LocalDateTime){
        coroutineScope.launch {
            order.value?.date = ldt
            checkEnable()
        }
    }

    fun redactAnalysis(analysis: Analysis, check: Boolean) {
        if (check){
            order.value?.price?.plus(analysis.price?:0)
        }else{
            order.value?.price?.minus(analysis.price?:0)
        }
    }

    fun saveAddress(a: Address) {
        coroutineScope.launch {
            val gson = Gson()
            Log.i("address", a.toString())
            saveAddress.request(context = context, gson.toJson(a))
            order.value?.clientAddress = a
        }
    }

    fun saveRecord(uri: Uri) {
        record.value = uri
        saveOrderRecord.request(context, uri.toString())
    }

    fun setting() {
        getShoppingCard()
        coroutineScope.launch {
            getAddress()
            order.postValue(order.value)
        }

    }
}

class PlaceOnOrderViewModelFabric(_context: Context): ViewModelProvider.Factory{
    val context = _context
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PlaceOnOrderViewModel(context = context) as T
    }
}