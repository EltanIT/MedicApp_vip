package com.example.medicapp_vip.view.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.ViewModelFactoryDsl
import com.example.medicapp_vip.R
import com.example.medicapp_vip.databinding.FragmentAnalysisBinding
import com.example.medicapp_vip.db.repository.GetAnalysisRepository
import com.example.medicapp_vip.db.repository.GetNewsRepository
import com.example.medicapp_vip.db.repositoryLocal.GetShoppingCard
import com.example.medicapp_vip.db.repositoryLocal.SaveShoppingCard
import com.example.medicapp_vip.objects.Analysis
import com.example.medicapp_vip.objects.News
import com.example.medicapp_vip.view.adapter.AnalysisAdapter
import com.example.medicapp_vip.view.adapter.CategoryAdapter
import com.example.medicapp_vip.view.adapter.NewsAdapter
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.reflect.Array
import java.lang.reflect.Type

class AnalysisFragment : Fragment() {

    private lateinit var binding: FragmentAnalysisBinding
    private lateinit var vm: AnalysisViewModel

    private lateinit var analysisClickListener: AnalysisClickListener
    private lateinit var shoppingCardAnalysisListener: ShoppingCardAnalysisListener
    private lateinit var analysisAdapter: AnalysisAdapter

    private val shoppingCard: ArrayList<Analysis> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAnalysisBinding.inflate(layoutInflater)
        vm = ViewModelProvider(this, AnalysisViewModelFabric(requireContext()))[AnalysisViewModel::class.java]
        interfaceSetting()
        setting()
        subscriptions()
        return binding.root
    }

    private fun setting() {
        binding.shoppingView.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_screen_all_view, ShoppingCardFragment(shoppingCardAnalysisListener))
                .addToBackStack("shoppingCard")
                .commit()
        }
    }

    private fun interfaceSetting() {
        analysisClickListener = object : AnalysisClickListener{

            override fun clickItem(analysis: Analysis) {
                val bundle = Bundle()
                val gson = Gson()
                val frag = ShowAnalysisDataFragment()
                bundle.putBoolean("state", shoppingCard.contains(analysis))
                bundle.putString("analysis", gson.toJson(analysis))

                frag.arguments = bundle
                requireActivity().supportFragmentManager.beginTransaction()
                    .add(R.id.main_screen_all_view,frag)
                    .addToBackStack("dataAnalysis")
                    .commit()

            }

            override fun clickAdd(analysis: Analysis) {
                vm.addItemInShoppingCard(analysis)
                redactPrice(analysis.price, true)
            }

            override fun clickDelete(analysis: Analysis) {
                vm.deleteItemFromShoppingCard(analysis)
                redactPrice(analysis.price, false)
            }

            private fun redactPrice(price: Int, state: Boolean) {
                var priceNow = binding.priceCard.text.toString().toInt()

                if (state){
                    priceNow += price
                }
                else{
                    priceNow -= price
                }

                if (priceNow > 0){
                    binding.shoppingView.visibility = VISIBLE
                }
                else{
                    binding.shoppingView.visibility = GONE
                }

                binding.priceCard.text = priceNow.toString()
            }

        }

        shoppingCardAnalysisListener = object : ShoppingCardAnalysisListener{
            override fun deleteItem(analysis: Analysis) {
                vm.deleteItemFromShoppingCard(analysis)
                analysisAdapter.deleteItemFromCard(analysis)
            }

            override fun deleteAll() {
                vm.clearShoppingCard()
                analysisAdapter.clearCard()

                vm.getShoppingCard()
            }


        }

    }

    private fun subscriptions() {
        vm.analysisList.observe(viewLifecycleOwner){
            if (it != null){
                analysisAdapter = AnalysisAdapter(it, analysisClickListener, shoppingCard)
                binding.analysisRv.adapter = analysisAdapter
            }

        }

        vm.shoppingCard.observe(viewLifecycleOwner){
            Log.i("shoppingCard", "$it")
            if (it != null){
                shoppingCard.addAll(it)
                shoppingCard.toArray()
                var priceCard = 0
                for (i in shoppingCard){
                    priceCard += i.price
                }
                if (priceCard > 0){
                    binding.shoppingView.visibility = VISIBLE
                }
                binding.priceCard.text = priceCard.toString()
            }
        }

        vm.categoryList.observe(viewLifecycleOwner){
            if (it != null){
                binding.categoryRv.adapter = CategoryAdapter(it)
            }
        }

        vm.newsList.observe(viewLifecycleOwner){
            if (it != null){
                binding.newsRv.adapter = NewsAdapter(it)
            }
        }
    }

    override fun onResume() {
        vm.getLists()
        super.onResume()
    }

    interface AnalysisClickListener{
        fun clickItem(analysis: Analysis)
        fun clickAdd(analysis: Analysis)
        fun clickDelete(analysis: Analysis)
    }

    interface ShoppingCardAnalysisListener{
        fun deleteItem(analysis: Analysis)
        fun deleteAll()
    }

}

class AnalysisViewModel(_context: Context): ViewModel() {
    val context = _context

    val analysisList = MutableLiveData<ArrayList<Analysis>>()
    val shoppingCard = MutableLiveData<ArrayList<Analysis>>()

    val categoryList = MutableLiveData<ArrayList<String>>()
    val newsList = MutableLiveData<ArrayList<News>>()

    private val getAnalysisRepository = GetAnalysisRepository()
    private val getShoppingCard = GetShoppingCard()
    private val saveShoppingCard = SaveShoppingCard()
    private val getNewsRepository = GetNewsRepository()

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private fun addItems(){
        val list = ArrayList<Analysis>()
        list.add(Analysis("fdaf", "fdafa", "fdafdas", "2 дня", "Кровь", 1000, "fda" ))
        list.add(Analysis("fdaf", "fdafa", "fdafdas", "2 дня", "Кровь", 1000, "fda" ))
        list.add(Analysis("fdaf", "fdafa", "fdafdas", "2 дня", "Кровь", 1000, "fda" ))
        list.add(Analysis("fdaf", "fdafa", "fdafdas", "2 дня", "Кровь", 1000, "fda" ))
        list.add(Analysis("fdaf", "fdafa", "fdafdas", "2 дня", "Кровь", 1000, "fda" ))
        list.add(Analysis("fdaf", "fdafa", "fdafdas", "2 дня", "Кровь", 1000, "fda" ))
        list.add(Analysis("fdaf", "fdafa", "fdafdas", "2 дня", "Кровь", 1000, "fda" ))
        list.add(Analysis("fdaf", "fdafa", "fdafdas", "2 дня", "Кровь", 1000, "fda" ))
        list.add(Analysis("fdaf", "fdafa", "fdafdas", "2 дня", "Кровь", 1000, "fda" ))
        list.add(Analysis("fdaf", "fdafa", "fdafdas", "2 дня", "Кровь", 1000, "fda" ))
        list.add(Analysis("fdaf", "fdafa", "fdafdas", "2 дня", "Кровь", 1000, "fda" ))
        list.add(Analysis("fdaf", "fdafa", "fdafdas", "2 дня", "Кровь", 1000, "fda" ))
        list.add(Analysis("fdaf", "fdafa", "fdafdas", "2 дня", "Кровь", 1000, "fda" ))
        analysisList.postValue(list)
    }
    private fun getCategory(){
        coroutineScope.launch {
            val list = ArrayList<String>()
            list.add("Популярные")
            list.add("Covid")
            list.add("Комплексные")
            categoryList.postValue(list)
        }

    }

    private fun getAnalysisList(){
            val body = getAnalysisRepository.request()
            if (body != null){
                val objectMapper = ObjectMapper()

                val listType = objectMapper.typeFactory.constructCollectionType(
                    ArrayList::class.java,
                    Analysis::class.java
                )
                val list: ArrayList<Analysis>? = objectMapper.readValue(body, listType)
                if (list != null){
                    analysisList.postValue(list)
                }
                else{
                    analysisList.postValue(ArrayList())
                }
            }
            else{
                analysisList.postValue(null)
            }

//            addItems()
    }

    fun getLists(){
        getCategory()
        coroutineScope.launch {
            getShoppingCard()
            getAnalysisList()
        }
        getNewsList()
    }
    fun getNewsList(){
        coroutineScope.launch {
            val body = getNewsRepository.request()
            if (body != null){
                val objectMapper = ObjectMapper()

                val listType = objectMapper.typeFactory.constructCollectionType(
                    ArrayList::class.java,
                    News::class.java
                )
                val list: ArrayList<News>? = objectMapper.readValue(body, listType)
                if (list != null){
                    newsList.postValue(list)
                }
                else{
                    newsList.postValue(ArrayList())
                }
            }
            else{
                newsList.postValue(null)
            }
        }
    }

    fun getShoppingCard(){
            val body = getShoppingCard.request(context = context)
            val gson = Gson()
            if (body!= null){
                val type = object: TypeToken<List<Analysis>>() {}.type
                shoppingCard.postValue(gson.fromJson<ArrayList<Analysis>>(body, type))
                Log.i("shoppingCard", shoppingCard.value.toString())
            }
            else{
                val list = ArrayList<Analysis>()
                shoppingCard.postValue(list)
            }
    }

    fun saveShoppingCard(){
        coroutineScope.launch {
            val gson = Gson()
            Log.i("shoppingCard", shoppingCard.value.toString())
            val type = object: TypeToken<ArrayList<Analysis>>() {}.type
            saveShoppingCard.request(context = context, gson.toJson(shoppingCard.value, type))
        }
    }
    fun addItemInShoppingCard(analysis: Analysis){
        coroutineScope.launch {
            shoppingCard.value?.add(analysis)
            saveShoppingCard()
        }
    }
    fun deleteItemFromShoppingCard(analysis: Analysis){
        coroutineScope.launch {
            shoppingCard.value?.removeIf {
                it.title.equals(analysis.title)
            }
            saveShoppingCard()
        }
    }
    fun clearShoppingCard(){
        coroutineScope.launch {
            shoppingCard.value?.clear()
            saveShoppingCard()
        }
    }
}

class AnalysisViewModelFabric(_context: Context): ViewModelProvider.Factory{
    val context = _context
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AnalysisViewModel(_context = context) as T
    }


}