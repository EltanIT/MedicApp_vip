package com.example.medicapp_vip.view.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.medicapp_vip.R
import com.example.medicapp_vip.databinding.ActivityMainScreenBinding
import com.example.medicapp_vip.view.fragments.AnalysisFragment
import com.example.medicapp_vip.view.fragments.ProfileFragment
import com.google.android.material.navigation.NavigationBarView

class MainScreenActivity : AppCompatActivity() {

    private val analysisFragment = AnalysisFragment()
    private val profileFragment = ProfileFragment()
    private var activeFragment: Fragment = analysisFragment

    private var profileAddState = false


    private lateinit var binding: ActivityMainScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainScreenBinding.inflate(layoutInflater)
        setting()
        addFragment(analysisFragment)
        selectFragment(analysisFragment)
        setContentView(binding.root)
    }

    private fun selectFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .hide(activeFragment).show(fragment).commit()
    }
    private fun addFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .add(R.id.main_screen_view, fragment).hide(fragment)
            .commit()
    }

    private fun setting() {
        binding.bottomNav.setOnItemSelectedListener(object: OnItemSelectedListener,
            NavigationBarView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when(item.itemId){
                    R.id.home_item -> {
                        selectFragment(analysisFragment)
                        activeFragment = analysisFragment
                        return true
                    }
                    R.id.profile_item -> {
                        if (profileAddState){
                            selectFragment(profileFragment)
                            activeFragment = profileFragment
                        }
                        else{
                            addFragment(profileFragment)
                            selectFragment(profileFragment)
                            activeFragment = profileFragment
                            profileAddState = true
                        }

                        return true
                    }
                }
                return false
            }

        })
    }

    override fun onBackPressed() {
        val fragment =supportFragmentManager.findFragmentByTag("payment")

        super.onBackPressed()
    }

    interface BackPressedListener{
        fun onBackPressed(): Boolean
    }
}