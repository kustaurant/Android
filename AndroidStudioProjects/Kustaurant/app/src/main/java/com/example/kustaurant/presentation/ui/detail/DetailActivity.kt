package com.example.kustaurant.presentation.ui.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.kustaurant.R
import com.example.kustaurant.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailBinding
    private val detailViewModel : DetailViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        binding.lifecycleOwner = this
        binding.detailViewModel = detailViewModel

        initTabView()
    }

    private fun initTabView() {
        binding.vpMenuReview.adapter = MeReVPAdapter(this)

        detailViewModel.tabTitles.observe(this) { tabs ->
            TabLayoutMediator(binding.tlMenuReview, binding.vpMenuReview){ tab, position ->
                tab.text = tabs[position]
            }.attach()

        }
    }
}