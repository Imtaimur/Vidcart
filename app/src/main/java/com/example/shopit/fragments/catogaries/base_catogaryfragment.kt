package com.example.shopit.fragments.catogaries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopit.R
import com.example.shopit.adapters.BestProductAdapter
import com.example.shopit.adapters.BestdealsAdopter
import com.example.shopit.databinding.FragmentBasecatogaryBinding
import com.example.shopit.utils.showbottomnavigation

open   class  base_catogaryfragment:Fragment(R.layout.fragment_basecatogary) {
    private lateinit var binding:FragmentBasecatogaryBinding
    protected  val offerAdapter:BestProductAdapter by lazy { BestProductAdapter() }
    protected val bestProductAdapter: BestProductAdapter by lazy { BestProductAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentBasecatogaryBinding.inflate(inflater)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOfferRv()
        setupBestProductRv()
        offerAdapter.onClick={
            val b= Bundle().apply { putParcelable("product",it) }
            findNavController().navigate(R.id.productdetailfragment,b)
        }
        bestProductAdapter.onClick={
            val b= Bundle().apply { putParcelable("product",it) }
            findNavController().navigate(R.id.productdetailfragment,b)
        }



    }


    private fun setupBestProductRv() {

        binding.bestproduct.apply {
            layoutManager=
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
            adapter=bestProductAdapter


        }
    }

    private fun setupOfferRv() {

        binding.rvoffer.apply {
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter=offerAdapter
        }
    }
    fun shoofferloading(){
        binding.chairtprogressBar.visibility=View.VISIBLE

    }
    fun hidefferloading(){
        binding.chairtprogressBar.visibility=View.INVISIBLE

    }

    override fun onResume() {
        super.onResume()
        showbottomnavigation()
    }



}
