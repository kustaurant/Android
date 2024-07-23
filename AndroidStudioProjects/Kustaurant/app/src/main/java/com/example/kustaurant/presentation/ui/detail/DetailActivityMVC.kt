package com.example.kustaurant.presentation.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kustaurant.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivityMVC : AppCompatActivity() {
    private lateinit var binding : ActivityDetailBinding
    private val tabList = arrayListOf("메뉴","리뷰")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initTabView()
        initTierRecyclerView()
    }

    private fun initTierRecyclerView() {

    }

    private fun initTabView() {
        binding.vpMenuReview.adapter = MeReVPAdapter(this)

        TabLayoutMediator(binding.tlMenuReview, binding.vpMenuReview){ tab, position ->
            tab.text = tabList[position]
        }.attach()
    }
}