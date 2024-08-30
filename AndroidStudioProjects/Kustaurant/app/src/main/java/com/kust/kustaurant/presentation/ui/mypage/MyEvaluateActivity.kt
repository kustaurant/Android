package com.kust.kustaurant.presentation.ui.mypage

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kust.kustaurant.R
import com.kust.kustaurant.databinding.ActivityMyEvaluateBinding

class MyEvaluateActivity : AppCompatActivity() {
    lateinit var binding : ActivityMyEvaluateBinding
    private lateinit var evaluateRestaurantAdapter : SaveRestaurantAdapter
    private val viewModel: MyPageViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyEvaluateBinding.inflate(layoutInflater)
    }
}