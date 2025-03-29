package com.example.shopit.fragments.catogaries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopit.R
import com.example.shopit.adapters.BestProductAdapter
import com.example.shopit.adapters.BestdealsAdopter
import com.example.shopit.databinding.FragmentBasecatogaryBinding

open   class  base_catogaryfragment:Fragment(R.layout.fragment_basecatogary) {
    private lateinit var binding:FragmentBasecatogaryBinding
    private lateinit var offerAdapter:BestProductAdapter
    private lateinit var bestProductAdapter: BestProductAdapter

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
    }

    private fun setupBestProductRv() {
        bestProductAdapter= BestProductAdapter()
        binding.bestproduct.apply {
            layoutManager=
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
            adapter=bestProductAdapter


        }
    }

    private fun setupOfferRv() {
        offerAdapter= BestProductAdapter()
        binding.bestproduct.apply {
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter=offerAdapter
        }
    }


}
