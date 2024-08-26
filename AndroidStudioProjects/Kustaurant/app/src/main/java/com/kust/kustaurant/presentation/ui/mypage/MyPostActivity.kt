package com.kust.kustaurant.presentation.ui.mypage

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kust.kustaurant.R
import com.kust.kustaurant.databinding.ActivityMyPostBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPostActivity : AppCompatActivity() {
    lateinit var binding : ActivityMyPostBinding
    private val viewModel: MyPageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPostBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }
}