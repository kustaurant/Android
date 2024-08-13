package com.example.kustaurant.presentation.ui.mypage

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kustaurant.R
import com.example.kustaurant.databinding.ActivityMyScrapBinding

class MyScrapActivity : AppCompatActivity() {
    lateinit var binding : ActivityMyScrapBinding
    private var scrapData : ArrayList<ScrapData> = arrayListOf()
    private var scrapAdapter : ScrapAdapter ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyScrapBinding.inflate(layoutInflater)

        initRecyclerView()

        setContentView(binding.root)

    }

    private fun initRecyclerView() {


    }
}