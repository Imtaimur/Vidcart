package com.example.shopit.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.shopit.R
import com.example.shopit.databinding.ActivityShopingBinding
import com.example.shopit.viewmodel.cartViewmodel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.rpc.context.AttributeContext.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ShopingActivity : AppCompatActivity() {
    val binding by  lazy { ActivityShopingBinding.inflate(layoutInflater) }
    val viewmodel by viewModels<cartViewmodel> ()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.shoppingHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigation.setupWithNavController(navController)

        lifecycleScope.launchWhenStarted {
            viewmodel.cartProducts.collectLatest {
                when (it) {
                    is com.example.shopit.utils.Resource.Success -> {
                        val count = it.data?.size ?: 0
                        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
                        bottomNavigation.getOrCreateBadge(R.id.cartfragmet).apply {
                            number = count
                            backgroundColor = resources.getColor(R.color.g_gray500)
                        }
                    }
                    else -> Unit
                }
            }
        }
    }

}