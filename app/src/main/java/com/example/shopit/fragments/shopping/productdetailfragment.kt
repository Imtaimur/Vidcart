package com.example.shopit.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.example.shopit.R
import com.example.shopit.activities.ShopingActivity
import com.example.shopit.adapters.Viewpager2images
import com.example.shopit.data.CartProduct
import com.example.shopit.databinding.FragmetnProductDetailBinding
import com.example.shopit.databinding.ProductRvItemBinding
import com.example.shopit.utils.Resource
import com.example.shopit.utils.hidebottomnavigation

import com.example.shopit.viewmodel.DetailviewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class productdetailfragment:Fragment() {
    private val viewmModel by viewModels<DetailviewModel> ()
    private val args by navArgs<productdetailfragmentArgs>()
    private lateinit var binding:FragmetnProductDetailBinding
    private val viewPagerAdapter by lazy { Viewpager2images()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        hidebottomnavigation()

        binding= FragmetnProductDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val product=args.product
        setUpviewPager()
        binding.imageclose.setOnClickListener { findNavController().navigateUp()  }
        binding.apply {
            productname.text=product.name
            productprice.text="Rs ${product.price}"
            productdiscription.text=product.description
        }
        binding.addtocart.setOnClickListener {
            viewmModel.addupdateproductinCart(CartProduct(product,1,1))
        }
        lifecycleScope.launchWhenStarted {
            viewmModel.addtocart.collectLatest {
                when(it){
                   is Resource.Loading ->{
                        Snackbar.make(requireView(),"loading",Snackbar.LENGTH_SHORT).show()
                    }
                    is Resource.Success->{
                        Snackbar.make(requireView(),"Added",Snackbar.LENGTH_SHORT).show()

                    }
                    is Resource.Error->{
                        Snackbar.make(requireView(),it.message.toString(),Snackbar.LENGTH_SHORT).show()
                    }
                    else->Unit


                }

            }
        }
        viewPagerAdapter.differ.submitList(product.images)
    }


    private fun setUpviewPager() {
        binding.apply {
            viewpagerProductimages.adapter=viewPagerAdapter
        }
    }
}