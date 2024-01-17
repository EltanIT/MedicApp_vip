package com.example.medicapp_vip.view.adapter

import android.content.res.Resources
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.medicapp_vip.R
import com.example.medicapp_vip.databinding.ViewCategoryBinding

class CategoryAdapter(private val categoryList: ArrayList<String>): RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ViewCategoryBinding.bind(itemView)
        val resources: Resources = itemView.resources
    }

    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_category, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categoryList[position]

        if (holder.adapterPosition == selectedPosition){
            holder.binding.categoryBtn.isSelected = true
            holder.binding.categoryBtn.setTextColor(holder.resources.getColor(R.color.white))
        }
        else{
            holder.binding.categoryBtn.isSelected = false
            holder.binding.categoryBtn.setTextColor(holder.resources.getColor(R.color.category_txt_color))
        }

        holder.binding.categoryBtn.text = category

        holder.binding.categoryBtn.setOnClickListener {
            selectedPosition = holder.adapterPosition
            notifyDataSetChanged()
        }
    }
}