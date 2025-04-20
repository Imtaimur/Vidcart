package com.example.shopit.fragments.settings

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.shopit.databinding.FragmentUserAccountBinding
import com.example.shopit.utils.Resource
import com.example.shopit.viewmodel.UserAccountViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
@AndroidEntryPoint
class UserAccountFragment:Fragment() {
    private lateinit var binding:FragmentUserAccountBinding
    val viewModel by viewModels<UserAccountViewModel> ()
    private var imgUri: Uri?=null
    private lateinit var imageActvityResultLauncher:ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageActvityResultLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            imgUri=it.data?.data
            Glide.with(this).load(imgUri).into(binding.profilimge)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentUserAccountBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUser()
        lifecycleScope.launch {
            viewModel.user.collectLatest {
            when (it) {
                is Resource.Loading -> {
                    showuserLoading()

                }
                is Resource.Success -> {
                    hideuserLoading()
                    showuserInformation(it.data!!)

                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_SHORT).show()

                }
                else -> Unit
            }
        }

        }
        lifecycleScope.launchWhenStarted {
            viewModel.updateinfo.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        Toast.makeText(requireContext(),"Loadding",Toast.LENGTH_SHORT).show()

                    }
                    is Resource.Success -> {
                        Toast.makeText(requireContext(),"Success",Toast.LENGTH_SHORT).show()

                        showuserInformation(it.data!!)

                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_SHORT).show()

                    }
                    else -> Unit
                }
            }

            }
        binding.buttonSave.setOnClickListener {
            binding.apply {
                val firsntame=edFirstName.text.toString().trim()
                val seconname=edLastName.text.toString().trim()
                val email=edEmail.text.toString().trim()
                val user=com.example.shopit.data.User(firsntame,seconname,email)
                viewModel.updateuser(user,imgUri)
            }
        }
        binding.profilimge.setOnClickListener {
            val intent=Intent(Intent.ACTION_GET_CONTENT)
            intent.type="image/*"
            imageActvityResultLauncher.launch(intent)
        }
        }


    private fun showuserInformation(data:com.example.shopit.data.User) {
        binding.apply {
            Glide.with(this@UserAccountFragment).load((data.imgpath)).error(ColorDrawable(Color.BLACK)).into(profilimge)
            edFirstName.setText(data.firstname)
            edLastName.setText(data.lastname)
            edEmail.setText(data.email)
        }

    }

    private fun hideuserLoading() {
        binding.apply {
            progressbarAccount.visibility=View.GONE
            imageCloseUserAccount.visibility=View.VISIBLE
            profilimge.visibility=View.VISIBLE
            edFirstName.visibility=View.VISIBLE
            edLastName.visibility=View.VISIBLE
            tvUpdatePassword.visibility=View.VISIBLE
            buttonSave.visibility=View.VISIBLE
        }
    }

    private fun showuserLoading() {
      binding.apply {
          progressbarAccount.visibility=View.VISIBLE
          imageCloseUserAccount.visibility=View.INVISIBLE
          profilimge.visibility=View.INVISIBLE
          edFirstName.visibility=View.INVISIBLE
          edLastName.visibility=View.INVISIBLE
          tvUpdatePassword.visibility=View.INVISIBLE
          buttonSave.visibility=View.INVISIBLE

      }
    }
}