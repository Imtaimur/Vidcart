package com.example.shopit.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.shopit.R
import com.example.shopit.databinding.FragmentAccountOptionBinding

class Account_option_fragment: Fragment(R.layout.fragment_account_option) {
    private lateinit var binding:FragmentAccountOptionBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentAccountOptionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.registerbutton.setOnClickListener{
            findNavController().navigate(R.id.action_account_option_fragment_to_register_Fragment2)
        }
        binding.login.setOnClickListener{
            findNavController().navigate(R.id.action_account_option_fragment_to_login_Fragment)
        }
    }
}