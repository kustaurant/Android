package com.example.kustaurant.presentation.ui.detail

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kustaurant.R
import com.example.kustaurant.databinding.ActivityEvaluateBinding

class EvaluateActivity : AppCompatActivity() {
    lateinit var binding : ActivityEvaluateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEvaluateBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }
}