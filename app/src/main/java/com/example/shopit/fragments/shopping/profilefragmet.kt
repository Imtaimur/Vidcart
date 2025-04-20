package com.example.shopit.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.shopit.R
import com.example.shopit.databinding.FragmentProfileBinding

class profilefragmet: Fragment(R.layout.fragment_profile) {
    private lateinit var binding:FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.GotoProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profilefragmet_to_userAccountFragment)
        }
        binding.checkorders.setOnClickListener {
            findNavController().navigate(R.id.action_profilefragmet_to_orderFragment)
        }
    }

}