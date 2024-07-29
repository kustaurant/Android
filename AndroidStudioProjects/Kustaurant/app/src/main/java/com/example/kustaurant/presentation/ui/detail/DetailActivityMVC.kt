package com.example.kustaurant.presentation.ui.detail

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kustaurant.R
import com.example.kustaurant.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivityMVC : AppCompatActivity() {
    private lateinit var binding : ActivityDetailBinding
    private val tabList = arrayListOf("메뉴","리뷰")
    private lateinit var tierInfoAdapter : DetailTierInfoAdapter
    private val tierData : ArrayList<TierInfoData> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initTabView()

        initDummyData()
        initTierRecyclerView()

        initEvaluate()

    }

    private fun initEvaluate() {
        binding.clEvaluate.setOnClickListener {
            val intent = Intent(this, EvaluateActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initDummyData() {
        tierData.addAll(
            arrayListOf(
                TierInfoData(R.drawable.img_category_korea, "한식", 1),
                TierInfoData(0, "혼밥", 0)
            )
        )
    }

    private fun initTierRecyclerView() {
        tierInfoAdapter = DetailTierInfoAdapter(this, tierData)
        binding.rvTier.adapter = tierInfoAdapter
        binding.rvTier.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun initTabView() {
        binding.vpMenuReview.adapter = MeReVPAdapter(this)

        TabLayoutMediator(binding.tlMenuReview, binding.vpMenuReview){ tab, position ->
            tab.text = tabList[position]
        }.attach()
    }
}