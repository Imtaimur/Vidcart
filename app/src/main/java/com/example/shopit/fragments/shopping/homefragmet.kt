package com.example.shopit.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import androidx.fragment.app.Fragment
import com.example.shopit.R
import com.example.shopit.adapters.Home_viewpageAdopter
import com.example.shopit.databinding.FragmentHomeBinding
import com.example.shopit.fragments.catogaries.ChairFragment
import com.example.shopit.fragments.catogaries.CuboardFragment
import com.example.shopit.fragments.catogaries.FragmentMainCatogary
import com.example.shopit.fragments.catogaries.FurnitureFragment
import com.example.shopit.fragments.catogaries.TableFragment
import com.example.shopit.fragments.catogaries.base_catogaryfragment
import com.google.android.material.tabs.TabLayoutMediator

class homefragmet: Fragment(R.layout.fragment_home) {
    private lateinit var binding:FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val catogaryFragments= arrayListOf<Fragment>(
        FragmentMainCatogary(),
        ChairFragment(),
        CuboardFragment(),
        FurnitureFragment(),
        TableFragment()
        )
        val viewpage2adpter=Home_viewpageAdopter(catogaryFragments,childFragmentManager,lifecycle)
        binding.viewpagerhome.adapter=viewpage2adpter
        TabLayoutMediator(binding.tableLayout,binding.viewpagerhome){
            tab,positon->
            when(positon){
                0->tab.text="Main"
                1->tab.text="Chair"
                2->tab.text="Cuboard"
                3->tab.text="Furniture"
                4->tab.text="Table"
                5->tab.text="Accessories"

            }

        }.attach()


    }
}