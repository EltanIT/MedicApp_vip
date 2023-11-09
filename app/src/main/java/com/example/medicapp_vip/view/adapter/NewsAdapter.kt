package com.example.medicapp_vip.view.adapter

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.medicapp_vip.R
import com.example.medicapp_vip.databinding.ViewAnalysisBinding
import com.example.medicapp_vip.databinding.ViewNewBinding
import com.example.medicapp_vip.objects.Analysis
import com.example.medicapp_vip.objects.News
import com.example.medicapp_vip.view.fragments.AnalysisFragment
import java.util.Base64

class NewsAdapter(private val newsList: ArrayList<News>): RecyclerView.Adapter<NewsAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ViewNewBinding.bind(itemView)
        val resources: Resources = itemView.resources
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_new, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val news = newsList[position]
        val decodedString: ByteArray? = Base64.getDecoder().decode(news.image)
        val decodedByte: Bitmap? = BitmapFactory.decodeByteArray(decodedString, 0, decodedString?.size!!)

        holder.binding.image.setImageBitmap(decodedByte)
    }


}