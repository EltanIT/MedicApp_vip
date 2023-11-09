package com.example.medicapp_vip.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.medicapp_vip.R
import com.example.medicapp_vip.databinding.ViewShoppingCardBinding
import com.example.medicapp_vip.objects.Analysis
import com.example.medicapp_vip.view.fragments.ShoppingCardFragment

class ShoppingCardAdapter(val cardList: ArrayList<Analysis>, val listeners: ShoppingCardFragment.ShoppingCardClickListener): RecyclerView.Adapter<ShoppingCardAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ViewShoppingCardBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_shopping_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cardList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val analysis = cardList[position]

        holder.binding.name.text = analysis.title
        holder.binding.days.text = analysis.deadline
        holder.binding.price.text = analysis.price.toString()

        holder.binding.addPatientButton.setOnClickListener{
            listeners.clickAdd(analysis)
            var countNow = holder.binding.countPatient.text.toString().toInt()

            countNow += 1

            holder.binding.countPatient.text = countNow.toString()


        }
        holder.binding.removePatientButton.setOnClickListener{
            listeners.clickRemove(analysis)
            var countNow = holder.binding.countPatient.text.toString().toInt()

            countNow -= 1

            holder.binding.countPatient.text = countNow.toString()
        }
        holder.binding.deleteItem.setOnClickListener{
//            deleteItem(holder.adapterPosition)
            listeners.clickDeleteItem(analysis)
        }

    }

    private fun deleteItem(pos: Int){
        cardList.removeAt(pos)
        notifyItemRemoved(pos)
    }


}