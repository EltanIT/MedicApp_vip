package com.example.medicapp_vip.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.medicapp_vip.R
import com.example.medicapp_vip.databinding.ViewSelectedPatientsBinding
import com.example.medicapp_vip.objects.Analysis
import com.example.medicapp_vip.objects.Profile
import com.example.medicapp_vip.view.fragments.PlaceOnOrderFragment

class OrderProfilesAdapter(var analysisList: ArrayList<Analysis>, val profiles: ArrayList<Profile>, val listeners: PlaceOnOrderFragment.OrderProfilesListeners): RecyclerView.Adapter<OrderProfilesAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ViewSelectedPatientsBinding.bind(itemView)
        val resources = itemView.resources
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_selected_patients, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return profiles.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val profile = profiles[position]
        holder.binding.analysisList.adapter = OrderProfilesAnalysisAdapter(analysisList)

        holder.binding.nameClose.text = "${profile.firstName} ${profile.lastName}"
        holder.binding.nameOpen.text = "${profile.firstName} ${profile.lastName}"

        if (profile.gender == 1){
            holder.binding.iconClose.text = "\uD83D\uDC69"
            holder.binding.iconOpen.text = "\uD83D\uDC69"
        }

        holder.binding.closeView.setOnClickListener{
            holder.binding.openView.visibility = VISIBLE
            holder.binding.closeView.visibility = GONE
        }
        holder.binding.patientOpenView.setOnClickListener{
            holder.binding.openView.visibility = GONE
            holder.binding.closeView.visibility = VISIBLE
        }

        holder.binding.delete.setOnClickListener {
            if (profiles.size != 1){
                listeners.deleteItem(profile)
            }

        }


    }

    @SuppressLint("NotifyDataSetChanged")
    fun notifyData(){
        notifyDataSetChanged()
    }


}