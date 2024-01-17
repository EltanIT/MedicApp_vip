package com.example.medicapp_vip.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.medicapp_vip.R
import com.example.medicapp_vip.databinding.ViewAnalysisBinding
import com.example.medicapp_vip.objects.Analysis
import com.example.medicapp_vip.view.fragments.AnalysisFragment

class AnalysisAdapter(val analysisList: ArrayList<Analysis>, val listeners: AnalysisFragment.AnalysisClickListener, val cardList: ArrayList<Analysis>): RecyclerView.Adapter<AnalysisAdapter.ViewHolder>(), Filterable {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ViewAnalysisBinding.bind(itemView)
        val resources = itemView.resources
    }

    private val filteredAnalysisList = analysisList
    private val fullList = ArrayList<Analysis>()

    init {
        fullList.addAll(analysisList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_analysis, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredAnalysisList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val analysis = filteredAnalysisList[position]

        if (cardList.size > 0){
            for (i in cardList){
                if (i.title.equals(analysis.title)){
                    holder.binding.add.isSelected = true
                    holder.binding.add.setTextColor(holder.resources.getColor(R.color.accent))
                    holder.binding.add.text = "Убрать"
                    break
                }else{
                    holder.binding.add.isSelected = false
                    holder.binding.add.setTextColor(holder.resources.getColor(R.color.white))
                    holder.binding.add.text = "Добавить"
                }
            }
        }else{
            holder.binding.add.isSelected = false
            holder.binding.add.setTextColor(holder.resources.getColor(R.color.white))
            holder.binding.add.text = "Добавить"
        }

        holder.binding.name.text = analysis.title
        holder.binding.days.text = analysis.deadline
        holder.binding.price.text = analysis.price.toString() + " ₽"
        holder.binding.add.setOnClickListener {
            val state = holder.binding.add.isSelected
            if (!state){
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
        notifyItemRangeChanged(0, analysisList.size-1)
    }

    fun notifyAdapter(){
        notifyDataSetChanged()
    }

    fun clearCard(){
        cardList.clear()
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return filter
    }

    private val filter = object: Filter(){
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = ArrayList<Analysis>()
            if (constraint.toString().toLowerCase().trim().isNullOrEmpty()){
                filteredList.addAll(fullList)
                Log.i("analysis", fullList.size.toString())
            }else{
                val filterPattern = constraint.toString().toLowerCase().trim()

                for (item in fullList){
                    if (item.title?.toLowerCase()?.contains(filterPattern) == true){
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            filteredAnalysisList.clear()
            filteredAnalysisList.addAll(results?.values as ArrayList<Analysis>)
            notifyDataSetChanged()
        }

    }

}