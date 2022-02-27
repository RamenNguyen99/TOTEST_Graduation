package com.example.totest

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.totest.databinding.ActivityMainBinding
import com.example.totest.ui.listtest.ListTestActivity

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        window.statusBarColor = resources.getColor(R.color.colorBlue)
        window.closeAllPanels()
        screenDelay()
    }

    private fun screenDelay() = Handler(Looper.getMainLooper()).postDelayed({
        startActivity(Intent(this, ListTestActivity::class.java))
        finish()
    }, 3000)
}