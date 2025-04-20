package com.example.shopit.utils

import android.view.View
import androidx.fragment.app.Fragment
import com.example.shopit.R
import com.example.shopit.activities.ShopingActivity
import com.example.shopit.databinding.FragmetnProductDetailBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


    fun Fragment.hidebottomnavigation(){
        val bottomNavigationView=(activity as ShopingActivity).findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigationView.visibility= View.GONE

    }
    fun Fragment.showbottomnavigation(){
        val bottomNavigationView=(activity as ShopingActivity).findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigationView.visibility= View.VISIBLE

    }
