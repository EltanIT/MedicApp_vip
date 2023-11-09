package com.example.medicapp_vip.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.medicapp_vip.R
import com.example.medicapp_vip.databinding.FragmentShowAnalysisDataBinding
import com.example.medicapp_vip.db.repositoryLocal.AddItemInShoppingCard
import com.example.medicapp_vip.objects.Analysis
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShowAnalysisDataFragment : Fragment() {

    private lateinit var binding: FragmentShowAnalysisDataBinding

    private lateinit var vm: ShowAnalysisDataViewModel

    private lateinit var analysis: Analysis


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShowAnalysisDataBinding.inflate(layoutInflater)
        vm = ViewModelProvider(this, ShowAnalysisDataViewModelFabric(requireContext()))[ShowAnalysisDataViewModel::class.java]
        subscriptions()
        bundle()
        setting()
        return binding.root
    }


    private fun subscriptions() {
        vm.result.observe(viewLifecycleOwner){
            binding.addButton.isSelected = it
        }

    }

    private fun bundle() {
        val bundle = arguments
        if (bundle!= null){
            val gson = Gson()
            analysis = gson.fromJson(bundle.getString("analysis", null), Analysis::class.java)
            val state = bundle.getBoolean("state")
            binding.addButton.isSelected = state
            if (state){
                binding.addButton.setTextColor(resources.getColor(R.color.accent))
            }
            else{
                binding.addButton.setTextColor(resources.getColor(R.color.white))
            }

            if (analysis!= null){
                binding.addButton.text = "Добавить за ${analysis.price} ₽"
                binding.name.text = analysis.title
                binding.description.text = analysis.description
                binding.description2.text = analysis.processDescription
                binding.material.text = analysis.material
                binding.dayCount.text = analysis.deadline
            }
        }
    }

    private fun setting() {
        binding.closeDataView.setOnClickListener{
            closeView()
        }
        binding.closeView.setOnClickListener{
            closeView()
        }

        binding.addButton.setOnClickListener{
            if (!binding.addButton.isSelected){
                vm.addItemInShoppingCard(analysis)
            }
            else{
                vm.deleteItemFromShoppingCard(analysis)
            }

        }
    }

    override fun onResume() {
        openView()
        super.onResume()
    }

    private fun openView(){
        val animation = AnimationUtils.loadAnimation(context,R.anim.open_view)
        binding.cardView.startAnimation(animation)
    }

    private fun closeView(){
        val animation = AnimationUtils.loadAnimation(context,R.anim.close_view)
        animation.setAnimationListener(object: AnimationListener{
            override fun onAnimationStart(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                requireActivity().supportFragmentManager.popBackStack()
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }

        })
        binding.cardView.startAnimation(animation)
    }
}

class ShowAnalysisDataViewModel(_context: Context): ViewModel() {
    val context = _context

    val result = MutableLiveData<Boolean>()

    private val addItemInShoppingCard = AddItemInShoppingCard()

    private val coroutineScope = CoroutineScope(Dispatchers.IO)


    fun addItemInShoppingCard(analysis: Analysis){
        coroutineScope.launch {
            result.postValue(addItemInShoppingCard.request(context = context, analysis))
        }
    }
    fun deleteItemFromShoppingCard(analysis: Analysis){
        coroutineScope.launch {
            result.postValue(addItemInShoppingCard.request(context = context, analysis))
        }
    }
}

class ShowAnalysisDataViewModelFabric(_context: Context): ViewModelProvider.Factory{
    val context = _context
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ShowAnalysisDataViewModel(_context = context) as T
    }


}