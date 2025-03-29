package com.example.shopit.fragments.catogaries

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.shopit.R
import com.example.shopit.adapters.BestProductAdapter
import com.example.shopit.adapters.BestdealsAdopter
import com.example.shopit.adapters.SpeacialProductAdapter
import com.example.shopit.databinding.FragmentHomeBinding
import com.example.shopit.databinding.FragmentMaincatogaryBinding
import com.example.shopit.databinding.SpecialRvItemBinding
import com.example.shopit.utils.Resource
import com.example.shopit.viewmodel.MainCatogaryViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.log
 private val TAG= "FragmentMainCatogary"

@AndroidEntryPoint
class FragmentMainCatogary:Fragment(R.layout.fragment_maincatogary) {
    private lateinit var binding: FragmentMaincatogaryBinding
    private lateinit var speacialProductAdapter: SpeacialProductAdapter
    private lateinit var bestProductAdapter: BestProductAdapter
    private lateinit var bestdealsAdopter: BestdealsAdopter
    private val viewModel by viewModels<MainCatogaryViewModel> ()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentMaincatogaryBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpSpecialproduct()
        setUpBestDealsRv()
        setUpBestProductRV()
        lifecycleScope.launchWhenStarted {
            viewModel.specialProducts.collectLatest {
                when(it){
                    is Resource.Loading->{
                        showLoading()

                    }
                    is Resource.Success->{
                        speacialProductAdapter.difer.submitList(it.data)
                        hideLoading()
                    }
                    is Resource.Error->{
                        hideLoading()
                        Log.e(TAG,it.message.toString())
                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }

            }


        }
        lifecycleScope.launchWhenStarted {
            viewModel.bestproduct.collectLatest {
                when(it){
                    is Resource.Loading->{
                        showLoading()

                    }
                    is Resource.Success->{
                        bestProductAdapter.differ.submitList(it.data)
                        hideLoading()
                    }
                    is Resource.Error->{
                        hideLoading()
                        Log.e(TAG,it.message.toString())
                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }

            }


        }
        lifecycleScope.launchWhenStarted {
            viewModel.bestdealProduct.collectLatest {
                when(it){
                    is Resource.Loading->{
                        showLoading()

                    }
                    is Resource.Success->{
                        bestdealsAdopter.differ.submitList(it.data)
                        hideLoading()
                    }
                    is Resource.Error->{
                        hideLoading()
                        Log.e(TAG,it.message.toString())
                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }

            }


        }
    }


    private fun setUpBestProductRV() {
        bestProductAdapter= BestProductAdapter()
         binding.bestdealsadapter.apply {
             layoutManager=GridLayoutManager(requireContext(),2,GridLayoutManager.VERTICAL,false)
             adapter=bestProductAdapter




         }
    }

    private fun setUpBestDealsRv() {
        bestdealsAdopter= BestdealsAdopter()
        binding.bestdealsadapter.apply {
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter=bestdealsAdopter


        }
    }

    private fun hideLoading() {
        binding.progressBar.visibility=View.INVISIBLE
    }

    private fun showLoading() {
        binding.progressBar.visibility=View.VISIBLE
    }

    private fun setUpSpecialproduct() {
        speacialProductAdapter= SpeacialProductAdapter()
        binding.bestdealsadapter.apply {
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter=speacialProductAdapter

        }


    }

}