package com.example.medicapp_vip.view.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.medicapp_vip.R
import com.example.medicapp_vip.databinding.FragmentSelectedDateBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.Calendar

class SelectedDateFragment(val listener: PlaceOnOrderFragment.DateListener) : Fragment() {

    private lateinit var binding: FragmentSelectedDateBinding
    private lateinit var vm: SelectedDateViewModel

    private var day: Int = 0
    private lateinit var hour: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectedDateBinding.inflate(layoutInflater)
        vm = ViewModelProvider(this, SelectedDateViewModelFabric(requireContext()))[SelectedDateViewModel::class.java]
        setting()
        settingDateBinding()
        subscriptions()
        return binding.root
    }

    private fun settingDateBinding() {
        binding.time10.setOnClickListener {
            binding.time10.setTextColor(resources.getColor(R.color.white))
            binding.time13.setTextColor(resources.getColor(R.color.Caption))
            binding.time14.setTextColor(resources.getColor(R.color.Caption))
            binding.time15.setTextColor(resources.getColor(R.color.Caption))
            binding.time16.setTextColor(resources.getColor(R.color.Caption))
            binding.time18.setTextColor(resources.getColor(R.color.Caption))
            binding.time19.setTextColor(resources.getColor(R.color.Caption))
            binding.time10.isSelected = true
            binding.time13.isSelected = false
            binding.time14.isSelected = false
            binding.time15.isSelected = false
            binding.time16.isSelected = false
            binding.time18.isSelected = false
            binding.time19.isSelected = false
            binding.dateContinueButton.isEnabled = true

            hour = "10"
        }
        binding.time13.setOnClickListener {
            binding.time10.setTextColor(resources.getColor(R.color.Caption))
            binding.time13.setTextColor(resources.getColor(R.color.white))
            binding.time14.setTextColor(resources.getColor(R.color.Caption))
            binding.time15.setTextColor(resources.getColor(R.color.Caption))
            binding.time16.setTextColor(resources.getColor(R.color.Caption))
            binding.time18.setTextColor(resources.getColor(R.color.Caption))
            binding.time19.setTextColor(resources.getColor(R.color.Caption))
            binding.time10.isSelected = false
            binding.time13.isSelected = true
            binding.time14.isSelected = false
            binding.time15.isSelected = false
            binding.time16.isSelected = false
            binding.time18.isSelected = false
            binding.time19.isSelected = false
            binding.dateContinueButton.isEnabled = true

            hour = "13"
        }
        binding.time14.setOnClickListener {
            binding.time10.setTextColor(resources.getColor(R.color.Caption))
            binding.time13.setTextColor(resources.getColor(R.color.Caption))
            binding.time14.setTextColor(resources.getColor(R.color.white))
            binding.time15.setTextColor(resources.getColor(R.color.Caption))
            binding.time16.setTextColor(resources.getColor(R.color.Caption))
            binding.time18.setTextColor(resources.getColor(R.color.Caption))
            binding.time19.setTextColor(resources.getColor(R.color.Caption))
            binding.time10.isSelected = false
            binding.time13.isSelected = false
            binding.time14.isSelected = true
            binding.time15.isSelected = false
            binding.time16.isSelected = false
            binding.time18.isSelected = false
            binding.time19.isSelected = false
            binding.dateContinueButton.isEnabled = true

            hour = "14"
        }
        binding.time15.setOnClickListener {
            binding.time10.setTextColor(resources.getColor(R.color.Caption))
            binding.time13.setTextColor(resources.getColor(R.color.Caption))
            binding.time14.setTextColor(resources.getColor(R.color.Caption))
            binding.time15.setTextColor(resources.getColor(R.color.white))
            binding.time16.setTextColor(resources.getColor(R.color.Caption))
            binding.time18.setTextColor(resources.getColor(R.color.Caption))
            binding.time19.setTextColor(resources.getColor(R.color.Caption))
            binding.time10.isSelected = false
            binding.time13.isSelected = false
            binding.time14.isSelected = false
            binding.time15.isSelected = true
            binding.time16.isSelected = false
            binding.time18.isSelected = false
            binding.time19.isSelected = false
            binding.dateContinueButton.isEnabled = true

            hour = "15"
        }
        binding.time16.setOnClickListener {
            binding.time10.setTextColor(resources.getColor(R.color.Caption))
            binding.time13.setTextColor(resources.getColor(R.color.Caption))
            binding.time14.setTextColor(resources.getColor(R.color.Caption))
            binding.time15.setTextColor(resources.getColor(R.color.Caption))
            binding.time16.setTextColor(resources.getColor(R.color.white))
            binding.time18.setTextColor(resources.getColor(R.color.Caption))
            binding.time19.setTextColor(resources.getColor(R.color.Caption))
            binding.time10.isSelected = false
            binding.time13.isSelected = false
            binding.time14.isSelected = false
            binding.time15.isSelected = false
            binding.time16.isSelected = true
            binding.time18.isSelected = false
            binding.time19.isSelected = false
            binding.dateContinueButton.isEnabled = true

            hour = "16"
        }
        binding.time18.setOnClickListener {
            binding.time10.setTextColor(resources.getColor(R.color.Caption))
            binding.time13.setTextColor(resources.getColor(R.color.Caption))
            binding.time14.setTextColor(resources.getColor(R.color.Caption))
            binding.time15.setTextColor(resources.getColor(R.color.Caption))
            binding.time16.setTextColor(resources.getColor(R.color.Caption))
            binding.time18.setTextColor(resources.getColor(R.color.white))
            binding.time19.setTextColor(resources.getColor(R.color.Caption))
            binding.time10.isSelected = false
            binding.time13.isSelected = false
            binding.time14.isSelected = false
            binding.time15.isSelected = false
            binding.time16.isSelected = false
            binding.time18.isSelected = true
            binding.time19.isSelected = false
            binding.dateContinueButton.isEnabled = true

            hour = "18"
        }
        binding.time19.setOnClickListener {
            binding.time10.setTextColor(resources.getColor(R.color.Caption))
            binding.time13.setTextColor(resources.getColor(R.color.Caption))
            binding.time14.setTextColor(resources.getColor(R.color.Caption))
            binding.time15.setTextColor(resources.getColor(R.color.Caption))
            binding.time16.setTextColor(resources.getColor(R.color.Caption))
            binding.time18.setTextColor(resources.getColor(R.color.Caption))
            binding.time19.setTextColor(resources.getColor(R.color.white))
            binding.time10.isSelected = false
            binding.time13.isSelected = false
            binding.time14.isSelected = false
            binding.time15.isSelected = false
            binding.time16.isSelected = false
            binding.time18.isSelected = false
            binding.time19.isSelected = true
            binding.dateContinueButton.isEnabled = true

            hour = "19"
        }
    }

    private fun subscriptions() {
        vm.dateList.observe(viewLifecycleOwner){
            binding.dateSp.adapter = ArrayAdapter<String>(requireContext(), androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item, it)
        }
    }

    override fun onResume() {
        openView()
        vm.getDateList()
        super.onResume()
    }

    private fun setting() {
        binding.dateContinueButton.isEnabled = false
        binding.dateContinueButton.setOnClickListener {
            var ldt = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LocalDateTime.now()
            } else {
                TODO("VERSION.SDK_INT < O")
            }
            if (binding.time10.isSelected) {
                ldt = LocalDateTime.now().withHour(10).withMinute(0)
            } else if (binding.time13.isSelected) {
                ldt = LocalDateTime.now().withHour(13).withMinute(0)
            } else if (binding.time14.isSelected) {
                ldt = LocalDateTime.now().withHour(14).withMinute(0)
            } else if (binding.time15.isSelected) {
                ldt = LocalDateTime.now().withHour(15).withMinute(0)
            } else if (binding.time16.isSelected) {
                ldt = LocalDateTime.now().withHour(16).withMinute(0)
            } else if (binding.time18.isSelected) {
                ldt = LocalDateTime.now().withHour(18).withMinute(0)
            } else if (binding.time19.isSelected) {
                ldt = LocalDateTime.now().withHour(19).withMinute(0)
            }

            if (day == 1){
                ldt.dayOfMonth+1
            }
            val date = "${ldt.year}-${ldt.month}-${ldt.dayOfMonth}T${ldt.hour}:${ldt.minute}:${ldt.second}.0192"
            listener.execute(ldt)
            closeView()
        }

        binding.closeDateView.setOnClickListener {
            closeView()
        }
        binding.backgroundView.setOnClickListener {
            closeView()
        }

        binding.dateCardView.setOnClickListener {  }


        binding.dateSp.onItemSelectedListener = object: OnItemSelectedListener, OnItemClickListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                day = p2
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            }

        }
    }

    private fun openView(){
        val animation = AnimationUtils.loadAnimation(context, R.anim.open_view)
        binding.dateCardView.startAnimation(animation)
    }

    private fun closeView(){
        val animation = AnimationUtils.loadAnimation(context, R.anim.close_view)
        animation.setAnimationListener(object: Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                requireActivity().supportFragmentManager.popBackStack()
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }

        })
        binding.dateCardView.startAnimation(animation)
    }

}


class SelectedDateViewModel(_context: Context): ViewModel() {
    val context = _context

    val dateList = MutableLiveData<ArrayList<String>>()

    private val coroutineScope = CoroutineScope(Dispatchers.IO)


    fun getDateList(){
        coroutineScope.launch {
            val date = Calendar.getInstance()
            val list = ArrayList<String>()

            list.add("Сегодня ${date.get(Calendar.DAY_OF_MONTH)} ${date.get(Calendar.MONTH)}")
            list.add("Сегодня ${date.get(Calendar.DAY_OF_MONTH) + 1} ${date.get(Calendar.MONTH)}")

            dateList.postValue(list)
        }
    }
}

class SelectedDateViewModelFabric(_context: Context): ViewModelProvider.Factory{
    val context = _context
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SelectedDateViewModel(_context = context) as T
    }


}