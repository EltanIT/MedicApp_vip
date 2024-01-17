package com.example.medicapp_vip.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.medicapp_vip.R
import com.example.medicapp_vip.databinding.FragmentAddPatientBinding
import com.example.medicapp_vip.objects.Profile
import com.example.medicapp_vip.view.adapter.AddPatientAdapter

class AddPatientFragment(val listener: PlaceOnOrderFragment.AddPatientListener) : Fragment() {

    private lateinit var binding: FragmentAddPatientBinding

    private val patientList = ArrayList<Profile>()

    private lateinit var addPatientAdapter: AddPatientAdapter

    private lateinit var addPatientListener: PlaceOnOrderFragment.AddPatientListener

    private lateinit var selectedProfile: Profile

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddPatientBinding.inflate(layoutInflater)
        addList()
        listeners()
        setting()
        return binding.root
    }

    private fun listeners() {
        addPatientListener = object: PlaceOnOrderFragment.AddPatientListener {
            override fun execute(profile: Profile) {
                selectedProfile = profile
                binding.profileContinueButton.isEnabled = true
            }

        }
    }

    private fun setting() {
        binding.profileContinueButton.isEnabled = false
        binding.closeProfileView.setOnClickListener {
            closeView()
        }
        binding.backgroundView.setOnClickListener {
            closeView()
        }
        binding.profileContinueButton.setOnClickListener {
            listener.execute(profile = selectedProfile)
            closeView()
        }

        addPatientAdapter = AddPatientAdapter(patientList, addPatientListener)

        binding.patientsRv.adapter = addPatientAdapter
    }

    override fun onResume() {
        openView()
        super.onResume()
    }

    private fun addList() {
        patientList.add(Profile("Эдуард", "Тицкий", "fdjask", "fjdafja", 0))
        patientList.add(Profile("Елена", "Тицкая", "fdjask", "fjdafja", 1))
    }


    private fun openView(){
        val animation = AnimationUtils.loadAnimation(context, R.anim.open_view)
        binding.profileCardView.startAnimation(animation)
    }

    private fun closeView(){
        val animation = AnimationUtils.loadAnimation(context, R.anim.close_view)
        animation.setAnimationListener(object: Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                requireActivity().supportFragmentManager.popBackStack()
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }

        })
        binding.profileCardView.startAnimation(animation)
    }

}