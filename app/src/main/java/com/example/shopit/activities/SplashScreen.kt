package com.example.shopit.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.shopit.databinding.SplashScreenBinding

class SplashScreen : AppCompatActivity() {

    private lateinit var binding: SplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = SplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, ShopingActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}
