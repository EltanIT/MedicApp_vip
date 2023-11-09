package com.example.medicapp_vip.view.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.medicapp_vip.R
import com.example.medicapp_vip.databinding.FragmentShoppingCardBinding
import com.example.medicapp_vip.db.repository.GetAnalysisRepository
import com.example.medicapp_vip.db.repository.GetNewsRepository
import com.example.medicapp_vip.db.repositoryLocal.GetShoppingCard
import com.example.medicapp_vip.db.repositoryLocal.SaveShoppingCard
import com.example.medicapp_vip.objects.Analysis
import com.example.medicapp_vip.view.adapter.ShoppingCardAdapter
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShoppingCardFragment(val listener: AnalysisFragment.ShoppingCardAnalysisListener) : Fragment() {

    private lateinit var binding: FragmentShoppingCardBinding
    private lateinit var vm: ShoppingCardViewModel

    private lateinit var adapter: ShoppingCardAdapter
    private lateinit var shoppingCardClickListener: ShoppingCardClickListener


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShoppingCardBinding.inflate(layoutInflater)
        vm = ViewModelProvider(this, ShoppingCardViewModelFabric(requireContext()))[ShoppingCardViewModel::class.java]
        settingInterface()
        subscriptions()
        setting()
        return binding.root
    }

    private fun settingInterface() {
        shoppingCardClickListener = object: ShoppingCardClickListener{
            override fun clickAdd(analysis: Analysis) {
                redactPrice(analysis.price, true)
            }

            override fun clickRemove(analysis: Analysis) {
                redactPrice(analysis.price, false)

            }

            override fun clickDeleteItem(analysis: Analysis) {
                vm.deleteItemFromShoppingCard(analysis)
                listener.deleteItem(analysis)
                redactPrice(analysis.price, false)
            }

            fun redactPrice(price: Int, state: Boolean){
                var priceNow = binding.priceView.text.toString().toInt()

                if (state){
                    priceNow += price
                }
                else{
                    priceNow -= price
                }

                binding.priceView.text = priceNow.toString()
            }

        }
    }

    override fun onResume() {
        vm.getShoppingCard()
        super.onResume()
    }

    private fun subscriptions() {
        vm.shoppingCard.observe(viewLifecycleOwner){
            if (it.size != 0){
                adapter = ShoppingCardAdapter(it, shoppingCardClickListener)
                binding.cartRv.adapter = adapter
                var priceNow = 0
                for(i in it){
                    priceNow += i.price
                }
                binding.priceView.text = priceNow.toString()
            }
            else{
                requireActivity().supportFragmentManager.popBackStack()
            }

        }
    }

    private fun setting() {
        binding.deleteAll.setOnClickListener{
            vm.clearShoppingCard()
            listener.deleteAll()
        }
        binding.backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.continueButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_screen_all_view, PlaceOnOrderFragment())
                .addToBackStack("placeOnOrder")
                .commit()
        }
    }

    interface ShoppingCardClickListener{
        fun clickAdd(analysis: Analysis)
        fun clickRemove(analysis: Analysis)
        fun clickDeleteItem(analysis: Analysis)
    }

}


class ShoppingCardViewModel(_context: Context): ViewModel() {
    val context = _context

    val shoppingCard = MutableLiveData<ArrayList<Analysis>>()

    private val getShoppingCard = GetShoppingCard()
    private val saveShoppingCard = SaveShoppingCard()

    private val coroutineScope = CoroutineScope(Dispatchers.IO)


    fun getShoppingCard(){
        coroutineScope.launch {
            val body = getShoppingCard.request(context = context)
            val gson = Gson()
            if (body!= null){
                val type = object: TypeToken<List<Analysis>>() {}.type
                shoppingCard.postValue(gson.fromJson<ArrayList<Analysis>>(body, type))
            }
            else{
                val list = ArrayList<Analysis>()
                list.add(Analysis("fdaf", "fdafa", "fdafdas", "2 дня", "Кровь", 1000, "fda" ))
                shoppingCard.postValue(list)
                Log.i("shoppingCard", shoppingCard.value.toString())
            }
        }

    }

    fun saveShoppingCard(){
        coroutineScope.launch {
            val gson = Gson()
            val type = object: TypeToken<List<Analysis>>() {}.type
            Log.i("shoppingCard", shoppingCard.value.toString())
            saveShoppingCard.request(context = context, gson.toJson(shoppingCard.value, type))
        }
    }


    fun deleteItemFromShoppingCard(analysis: Analysis){
        coroutineScope.launch {
            shoppingCard.value?.remove(analysis)
            saveShoppingCard()
            if (shoppingCard.value?.size == 0){
                val list = ArrayList<Analysis>()
                shoppingCard.postValue(list)
            }
        }
    }

    fun clearShoppingCard(){
        coroutineScope.launch {
            shoppingCard.value?.clear()
            saveShoppingCard()
            if (shoppingCard.value?.size == 0){
                val list = ArrayList<Analysis>()
                shoppingCard.postValue(list)
            }
        }
    }
}

class ShoppingCardViewModelFabric(_context: Context): ViewModelProvider.Factory{
    val context = _context
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ShoppingCardViewModel(_context = context) as T
    }


}