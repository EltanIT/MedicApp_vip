package com.example.medicapp_vip.view.adapter

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

    private val list = ArrayList<Profile>()

    init {
        list.addAll(profiles)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_selected_patients, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val profile = list[position]

        val listenerAnalysis = object: PlaceOnOrderFragment.OrderProfilesListeners{
            override fun deleteItem(profile: Profile) {

            }

            override fun checkAnalysis(analysis: Analysis, check: Boolean) {
                listeners.checkAnalysis(analysis,check)
            }

        }
        holder.binding.analysisList.adapter = OrderProfilesAnalysisAdapter(analysisList, listenerAnalysis)

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
                delete(holder.adapterPosition)
                listeners.deleteItem(profile)
            }
        }

    }

    fun add(profile: Profile) {
        list.add(profile)
//        notifyItemRangeChanged(0,profiles.size)
        notifyItemInserted(profiles.size-1)
    }

    private fun delete(position: Int){
        list.removeAt(position)
//        notifyItemRangeChanged(0,profiles.size)
        notifyItemRemoved(position)
    }

}