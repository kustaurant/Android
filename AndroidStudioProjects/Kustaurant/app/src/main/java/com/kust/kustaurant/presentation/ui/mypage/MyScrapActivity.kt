package com.kust.kustaurant.presentation.ui.mypage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kust.kustaurant.databinding.ActivityMyScrapBinding

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