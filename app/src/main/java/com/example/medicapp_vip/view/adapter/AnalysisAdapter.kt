package com.example.medicapp_vip.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.medicapp_vip.R
import com.example.medicapp_vip.databinding.ViewAnalysisBinding
import com.example.medicapp_vip.objects.Analysis
import com.example.medicapp_vip.view.fragments.AnalysisFragment

class AnalysisAdapter(val analysisList: ArrayList<Analysis>, val listeners: AnalysisFragment.AnalysisClickListener, val cardList: ArrayList<Analysis>): RecyclerView.Adapter<AnalysisAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ViewAnalysisBinding.bind(itemView)
        val resources = itemView.resources
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_analysis, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return analysisList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val analysis = analysisList[position]

        if (cardList.isNotEmpty()){
            for (i in cardList){
                if (i.title.equals(analysis.title)){
                    holder.binding.add.isSelected = true
                    holder.binding.add.setTextColor(holder.resources.getColor(R.color.accent))
                    holder.binding.add.text = "Убрать"
                }
            }
        }

        holder.binding.name.text = analysis.title
        holder.binding.days.text = analysis.deadline
        holder.binding.price.text = analysis.price.toString()
        holder.binding.add.setOnClickListener {
            val state = holder.binding.add.isSelected
            if (!holder.binding.add.isSelected){
                holder.binding.add.setTextColor(holder.resources.getColor(R.color.accent))
                holder.binding.add.text = "Убрать"
                listeners.clickAdd(analysis)
            }
            else{
                holder.binding.add.setTextColor(holder.resources.getColor(R.color.white))
                holder.binding.add.text = "Добавить"
                listeners.clickDelete(analysis)
            }
            holder.binding.add.isSelected = !state

        }

        holder.itemView.setOnClickListener{
            listeners.clickItem(analysis)
        }

    }

    fun deleteItemFromCard(analysis: Analysis){
        cardList.removeIf {
            it.title.equals(analysis.title)
        }
        notifyDataSetChanged()
    }

    fun clearCard(){
        cardList.clear()
        notifyDataSetChanged()
    }


}