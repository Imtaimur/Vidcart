package com.example.shopit.fragments

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.shopit.R
import com.example.shopit.data.User
import com.example.shopit.databinding.FragmentRegisterBinding
import com.example.shopit.utils.RegisterValidation
import com.example.shopit.utils.Resource
import com.example.shopit.viewmodel.RegisterViewModel

import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.WithFragmentBindings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class Register_Fragment: Fragment(R.layout.fragment_register) {
    private lateinit var binding:FragmentRegisterBinding
    private val  viewModel by viewModels<RegisterViewModel> ()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.alreadyhaveaccount.setOnClickListener {
            findNavController().navigate(R.id.action_register_Fragment_to_login_Fragment)

        }
        binding.apply {registerbutton.setOnClickListener{
            val user=User(firstname.text.toString().trim(),
                secondname.text.toString(),
                registerEmail.text.toString().trim()
            )
            val password=registerpassword.text.toString()
            viewModel.createAccountwithEmailnPassword(user,password)
        }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.register.collect{
                when(it){
                    is Resource.Loading->{
                        Toast.makeText(context,"Loading",Toast.LENGTH_SHORT).show()
                    }
                is Resource.Success->{
                    Toast.makeText(context,"Account  Successfully Created ",Toast.LENGTH_SHORT).show();
                    findNavController().navigate(R.id.action_register_Fragment_to_login_Fragment)



                }
                    is Resource.Error->{
                        Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show();
                    }
                    else ->Unit
            }}

        }
        lifecycleScope.launchWhenStarted { viewModel.validation.collect{
            validation->
            if(validation.email is RegisterValidation.Failed){

                withContext(Dispatchers.Main){
                    binding.registerEmail.apply {
                        requestFocus()
                        error=validation.email.message
                    }
                }

            }
            if (validation.password is RegisterValidation.Failed){

                withContext(Dispatchers.Main){
                    binding.registerpassword.apply {
                        requestFocus()
                        error=validation.password.message
                    }
                }
            }
            if (validation.name is RegisterValidation.Failed){
                withContext(Dispatchers.Main){
                    binding.firstname.apply { requestFocus()
                    error=validation.name.message}
                }
            }
        }

        }

    }
}