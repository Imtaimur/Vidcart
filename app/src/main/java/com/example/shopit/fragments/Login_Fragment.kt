package com.example.shopit.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.shopit.R
import com.example.shopit.activities.ShopingActivity
import com.example.shopit.databinding.FragmentLoginBinding
import com.example.shopit.dialogue.setupBottomsheetDialog
import com.example.shopit.viewmodel.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.rpc.context.AttributeContext.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Login_Fragment: Fragment(R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvdonthaveaccount.setOnClickListener {
            findNavController().navigate(R.id.action_login_Fragment_to_register_Fragment)
        }
        binding.tvforgotpasswordlogin.setOnClickListener {
            setupBottomsheetDialog { email->
                viewModel.resetPassword(email)

            }

        }
        lifecycleScope.launchWhenStarted {viewModel.resetpassword.collect{
            when(it){
                is com.example.shopit.utils.Resource.Success->{
                    Snackbar.make(requireView(),"Reset Link was Sent to your E_Mail",Snackbar.LENGTH_SHORT).show()


                }
                is com.example.shopit.utils.Resource.Loading->{
                    Snackbar.make(requireView(),"Loading",Snackbar.LENGTH_SHORT).show()


                }
                is com.example.shopit.utils.Resource.Error->{
                    Snackbar.make(requireView(),"Error:${it.message}",Snackbar.LENGTH_SHORT).show()


                }

                else -> {}
            }





        }







        }
       binding.apply {
           binding.loginbutton.setOnClickListener{

               val email=emaillogin.text.trim()
               val password=paswordlogin.text.trim()
               viewModel.login(email,password)

           }
       }
        lifecycleScope.launchWhenStarted { viewModel.login.collect{
            when(it){
                is com.example.shopit.utils.Resource.Success->{
                    Toast.makeText(context,"LogIn successful",Toast.LENGTH_SHORT).show()
                    Intent(requireActivity(),ShopingActivity::class.java).also { intent ->
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }

                }
                is com.example.shopit.utils.Resource.Loading->{
                    Toast.makeText(context,"Loading",Toast.LENGTH_SHORT).show()

                }
                is com.example.shopit.utils.Resource.Error->{
                    Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()

                }

                else -> {}
            }
        } }
    }
}