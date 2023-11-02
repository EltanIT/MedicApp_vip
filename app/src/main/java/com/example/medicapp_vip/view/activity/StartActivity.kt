package com.example.medicapp_vip.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentPagerAdapter
import com.example.medicapp_vip.databinding.ActivityStartBinding
import com.example.medicapp_vip.view.adapter.HS_adapter
import com.example.medicapp_vip.view.fragments.Hello1Fragment
import com.example.medicapp_vip.view.fragments.Hello2Fragment
import com.example.medicapp_vip.view.fragments.Hello3Fragment

class StartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val hs_adapter = HS_adapter(supportFragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        hs_adapter.addFragment(Hello1Fragment())
        hs_adapter.addFragment(Hello2Fragment())
        hs_adapter.addFragment(Hello3Fragment())

        binding.viewPager.adapter = hs_adapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }
}