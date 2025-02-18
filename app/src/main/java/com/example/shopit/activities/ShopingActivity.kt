package com.example.shopit.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.shopit.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShopingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shoping)
    }
}