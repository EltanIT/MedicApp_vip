package com.example.medicapp_vip.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.medicapp_vip.R
import com.example.medicapp_vip.databinding.ViewAnalysisBinding
import com.example.medicapp_vip.databinding.ViewSelectedAnalysisBinding
import com.example.medicapp_vip.databinding.ViewSelectedPatientsBinding
import com.example.medicapp_vip.objects.Analysis
import com.example.medicapp_vip.objects.Profile
import com.example.medicapp_vip.view.fragments.AnalysisFragment
import com.example.medicapp_vip.view.fragments.PlaceOnOrderFragment

class OrderProfilesAnalysisAdapter(var analysisList: ArrayList<Analysis>, val listener: PlaceOnOrderFragment.OrderProfilesListeners): RecyclerView.Adapter<OrderProfilesAnalysisAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ViewSelectedAnalysisBinding.bind(itemView)
        val resources = itemView.resources
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_selected_analysis, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return analysisList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val analysis = analysisList[position]

        holder.binding.name.text = analysis.title
        holder.binding.priceView.text = "${analysis.price} ₽"

        holder.binding.analysisView.setOnClickListener {
            holder.binding.checkedAnalysis.isChecked = !holder.binding.checkedAnalysis.isChecked
        }

        holder.binding.checkedAnalysis.setOnCheckedChangeListener { compoundButton, b ->
            if (!b){
                holder.binding.name.setTextColor(holder.resources.getColor(R.color.Caption))
                holder.binding.priceView.setTextColor(holder.resources.getColor(R.color.Caption))
            }else{
                holder.binding.name.setTextColor(holder.resources.getColor(R.color.black))
                holder.binding.priceView.setTextColor(holder.resources.getColor(R.color.black))
            }
            listener.checkAnalysis(analysis, holder.binding.checkedAnalysis.isChecked)
        }



    }


}