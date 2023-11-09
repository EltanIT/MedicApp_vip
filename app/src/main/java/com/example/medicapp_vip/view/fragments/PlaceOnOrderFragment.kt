package com.example.medicapp_vip.view.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.medicapp_vip.R
import com.example.medicapp_vip.databinding.FragmentPlaceOnOrderBinding
import com.example.medicapp_vip.db.repositoryLocal.GetAddress
import com.example.medicapp_vip.db.repositoryLocal.GetShoppingCard
import com.example.medicapp_vip.db.repositoryLocal.SaveOrder
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
    }

    private lateinit var dateListener: DateListener
    private lateinit var addressListener: AddressListener
    private lateinit var addPatientListener: AddPatientListener
    private lateinit var orderProfilesListeners: OrderProfilesListeners

    private var orderProfilesAdapter: OrderProfilesAdapter? = null

    private lateinit var binding: FragmentPlaceOnOrderBinding
    private lateinit var vm: PlaceOnOrderViewModel

    private var price = 0
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
                vm.saveDate(date)
            }
        }

        addressListener = object : AddressListener {
            override fun execute(address: Address) {
                vm.getAddress()
            }
        }

        addPatientListener = object : AddPatientListener {
            override fun execute(profile: Profile) {
                vm.addPatient(profile)
            }
        }

        orderProfilesListeners = object : OrderProfilesListeners {
            override fun deleteItem(profile: Profile) {
                vm.deletePatient(profile)
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
            vm.saveOrder(binding.phoneNumber.text.toString(), price, binding.commentsEt.text.toString())
//            requireActivity().supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_screen_all_view, PaymentFragment())
                .addToBackStack("payment")
                .commit()
        }

        binding.phoneNumber.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                vm.savePhone(p0.toString())
            }

        })

        binding.commentsEt.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                vm.saveComment(p0.toString())
            }

        })

    }


    private fun subscriptions() {
        vm.address.observe(viewLifecycleOwner){
            if (it!= null){
                binding.addressTextView.text = "${it.address}, кв. ${it.flat}"
                binding.addressTextView.setTextColor(resources.getColor(R.color.black))
            }
            else{
            }
        }
        vm.date.observe(viewLifecycleOwner){
            if (it != null){
                binding.dateTextView.text = "$it"
            }
        }
        vm.shoppingCard.observe(viewLifecycleOwner){
            if (it != null){
                if (orderProfilesAdapter == null){
                    if (vm.patients.value != null){
                        orderProfilesAdapter = OrderProfilesAdapter(it, vm.patients.value!!, orderProfilesListeners)
                        binding.selectedPatientList.adapter = orderProfilesAdapter
                    }
                }
                else{
                    orderProfilesAdapter?.notifyData()
                }

                binding.countAnalysisView.text = it.size.toString()
                price = 0
                for (i in it){
                    price+=i.price
                }
                price *= vm.patients.value!!.size
                binding.priceView.text = price.toString()
            }
        }
//        vm.phone.observe(viewLifecycleOwner){
//            if (it != null){
//                binding.phoneNumber.setText(it)
//            }
//        }
//        vm.comment.observe(viewLifecycleOwner){
//            if (it != null){
//                binding.commentsEt.setText(it)
//            }
//        }

        vm.checkEnable.observe(viewLifecycleOwner){
            binding.continuePlaceOrderButton.isEnabled = it
        }
    }

    override fun onResume() {
        vm.getAddress()
        vm.getShoppingCard()
        super.onResume()
    }

}


class PlaceOnOrderViewModel(val context: Context): ViewModel() {

    val shoppingCard = MutableLiveData<ArrayList<Analysis>>()
    val address = MutableLiveData<Address>()
    val date = MutableLiveData<String>()
    val phone = MutableLiveData<String>()
    val comment = MutableLiveData<String>()
    val patients = MutableLiveData<ArrayList<Profile>>()
    val checkEnable = MutableLiveData<Boolean>()

    private var phoneText = ""
    private var commentText = ""

    private val getShoppingCard = GetShoppingCard()
    private val getAddress = GetAddress()
    private val saveOrder = SaveOrder()

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private fun checkEnable(){
        coroutineScope.launch {
            if (address.value != null
                && date.value?.isNotEmpty() == true
                && phoneText.length==10){

                phone.postValue(phoneText)
                comment.postValue(commentText)

                checkEnable.postValue(true)
            }
            else{
                checkEnable.postValue(false)
            }
        }
    }

    fun savePhone(number: String){
        phoneText = number
        checkEnable()

    }
    fun saveComment(com: String){
        commentText = com
    }

    fun addPatient(profile: Profile){
        patients.value?.add(profile)
        shoppingCard.value = shoppingCard.value
    }
    fun deletePatient(profile: Profile){
        patients.value?.removeIf {
            it == profile
        }
        shoppingCard.value = shoppingCard.value
    }

    fun saveOrder(toString: String, price: Int, toString1: String) {
        coroutineScope.launch {
            val gson = Gson()
            saveOrder.request(context, gson.toJson(getOrder(toString, price, toString1)))
        }
    }

    fun getShoppingCard(){
        coroutineScope.launch {
            val body = getShoppingCard.request(context = context)
            val gson = Gson()
            patients.postValue(ArrayList())
            patients.value?.add(Profile("Имя", "Фамилия", "fdasa", "f", 0))
            if (body!= null){
                val type = object: TypeToken<ArrayList<Analysis>>() {}.type
                shoppingCard.postValue(gson.fromJson(body, type))
            }
            else{
                val list = ArrayList<Analysis>()
                list.add(Analysis("fdaf", "fdafa", "fdafdas", "2 дня", "Кровь", 1000, "fda" ))
                shoppingCard.postValue(list)
                Log.i("shoppingCard", shoppingCard.value.toString())
            }
        }

    }
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
            checkEnable()
        }


    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun saveDate(ldt: LocalDateTime){
        coroutineScope.launch {
            val dateText = "Сегодня, ${ldt.dayOfMonth} ${ldt.month} ${ldt.hour} ${ldt.minute}"
            date.postValue(dateText)
            checkEnable()
        }
    }

    private fun getOrder(phone: String, price: Int, comment: String): Order {
        val objectMapper = ObjectMapper()
        val analysisList = ArrayList<Map<*, *>>()
        for (i in shoppingCard.value!!) {
            analysisList.add(objectMapper.convertValue(i, object : TypeReference<Map<*, *>>() {}))
        }
        return Order(
            objectMapper.convertValue(address.value, object : TypeReference<Map<*, *>>() {}),
            date.value,
            analysisList,
            phone,
            price,
            comment
        )
    }
}

class PlaceOnOrderViewModelFabric(_context: Context): ViewModelProvider.Factory{
    val context = _context
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PlaceOnOrderViewModel(context = context) as T
    }


}