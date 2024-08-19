package com.kust.kustaurant.presentation.ui.mypage

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kust.kustaurant.R
import com.kust.kustaurant.databinding.ActivityMyEditBinding

class MyEditActivity : AppCompatActivity() {
    lateinit var binding : ActivityMyEditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyEditBinding.inflate(layoutInflater)

        initBack()

        setContentView(binding.root)
    }

    private fun initBack() {
        binding.editBtnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}