package com.example.kustaurant.presentation.ui.detail

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kustaurant.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels()
    private lateinit var tierInfoAdapter: DetailTierInfoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.detailViewModel = viewModel
        binding.lifecycleOwner = this

        initTabView()
        initTierRecyclerView()
        initEvaluate()
    }

    private fun initEvaluate() {
        binding.btnEvaluate.setOnClickListener {
            val intent = Intent(this, EvaluateActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initTierRecyclerView() {
        viewModel.tierData.observe(this) { data ->
            tierInfoAdapter = DetailTierInfoAdapter(this, data)
            binding.rvTier.adapter = tierInfoAdapter
            binding.rvTier.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun initTabView() {
        viewModel.tabList.observe(this) { tabs ->
            binding.vpMenuReview.adapter = MeReVPAdapter(this)
            TabLayoutMediator(binding.tlMenuReview, binding.vpMenuReview) { tab, position ->
                tab.text = tabs[position]
            }.attach()
        }
    }
}
