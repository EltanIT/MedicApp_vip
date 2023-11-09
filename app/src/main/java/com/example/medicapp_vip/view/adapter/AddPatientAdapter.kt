package com.example.medicapp_vip.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.medicapp_vip.R
import com.example.medicapp_vip.databinding.ViewAddPatientBinding
import com.example.medicapp_vip.databinding.ViewAnalysisBinding
import com.example.medicapp_vip.objects.Analysis
import com.example.medicapp_vip.objects.Profile
import com.example.medicapp_vip.view.fragments.AddPatientFragment
import com.example.medicapp_vip.view.fragments.AnalysisFragment
import com.example.medicapp_vip.view.fragments.PlaceOnOrderFragment

class AddPatientAdapter(var profileList: ArrayList<Profile>, val listener: PlaceOnOrderFragment.AddPatientListener): RecyclerView.Adapter<AddPatientAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ViewAddPatientBinding.bind(itemView)
        val resources = itemView.resources
    }
    private var lastPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_add_patient, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return profileList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val profile = profileList[position]

        holder.binding.name.text = "${profile.lastName} ${profile.firstName}"

        if (profile.gender == 0){
            holder.binding.icon.text = "\uD83E\uDDD1"
        }
        else{
            holder.binding.icon.text = "\uD83D\uDC69"
        }

            if (holder.adapterPosition == lastPosition){
                holder.binding.backgroundView.isSelected = true
                holder.binding.name.setTextColor(holder.resources.getColor(R.color.white))
            }
             else{
                holder.binding.backgroundView.isSelected = false
                holder.binding.name.setTextColor(holder.resources.getColor(R.color.black))
            }

        holder.itemView.setOnClickListener {
            holder.binding.backgroundView.isSelected = true
            holder.binding.name.setTextColor(holder.resources.getColor(R.color.white))
            lastPosition = holder.adapterPosition
            notifyDataSetChanged()
            listener.execute(profile)
        }

    }


}