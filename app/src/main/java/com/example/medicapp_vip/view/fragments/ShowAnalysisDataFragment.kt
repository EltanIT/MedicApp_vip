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
import com.example.medicapp_vip.view.AnalysisDataListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShowAnalysisDataFragment(val listener: AnalysisDataListener) : Fragment() {

    private lateinit var binding: FragmentShowAnalysisDataBinding

    private lateinit var vm: ShowAnalysisDataViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShowAnalysisDataBinding.inflate(layoutInflater)
        vm = ViewModelProvider(this, ShowAnalysisDataViewModelFabric(requireContext(), listener))[ShowAnalysisDataViewModel::class.java]
        subscriptions()
        setting()
        vm.getBundle(arguments)
        return binding.root
    }


    private fun subscriptions() {
        vm.state.observe(viewLifecycleOwner){
            if (it!=null){
                binding.addButton.isSelected = it
                if (it){
                    binding.addButton.setTextColor(resources.getColor(R.color.accent))
                }else{
                    binding.addButton.setTextColor(resources.getColor(R.color.white))
                }
            }
        }

        vm.analysis.observe(viewLifecycleOwner){
            if (it!=null){
                binding.addButton.text = "Добавить за ${it.price} ₽"
                binding.name.text = it.title
                binding.description.text = it.description
                binding.description2.text = it.processDescription
                binding.material.text = it.material
                binding.dayCount.text = it.deadline
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
            vm.redactShoppingCard()
        }
        binding.cardView.setOnClickListener {}
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

class ShowAnalysisDataViewModel(val context: Context, val listener: AnalysisDataListener): ViewModel() {

    val state = MutableLiveData<Boolean>()
    val analysis = MutableLiveData(Analysis())

    fun getBundle(bundle: Bundle?){
        if (bundle!= null){
            val gson = Gson()
            val type = object: TypeToken<Analysis>() {}.type
            analysis.value = gson.fromJson(bundle.getString("analysis", null), type)
            state.value = bundle.getBoolean("state", false)
        }
    }

    fun redactShoppingCard() {
        if (state.value!=null){
            if (state.value == true){
                listener.delete(analysis.value!!)
            }else{
                listener.add(analysis.value!!)
            }
            state.value = !state.value!!
        }

    }
}

class ShowAnalysisDataViewModelFabric(val context: Context, val listener: AnalysisDataListener): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ShowAnalysisDataViewModel(context, listener) as T
    }


}