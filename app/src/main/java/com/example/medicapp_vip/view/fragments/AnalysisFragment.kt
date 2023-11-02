package com.example.medicapp_vip.view.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.medicapp_vip.R
import com.example.medicapp_vip.databinding.FragmentAnalisysBinding
import com.example.medicapp_vip.objects.Analysis

class AnalysisFragment : Fragment() {

    private lateinit var binding: FragmentAnalisysBinding
    private lateinit var vm: AnalysisViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAnalisysBinding.inflate(layoutInflater)
        vm = ViewModelProvider(this)[AnalysisViewModel::class.java]

        return binding.root
    }

}

class AnalysisViewModel(context: Context): ViewModel() {

    val analysisList = MutableLiveData<ArrayList<Analysis>>()

    fun getAnalysisList(){

    }
}