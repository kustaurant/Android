package com.example.kustaurant.presentation.ui.detail

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kustaurant.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels()
    private lateinit var tierInfoAdapter: DetailTierInfoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        initTabView()
        initTierRecyclerView()
        initNaverLink()
        changeTopBar()
        initEvaluate()
    }

    private fun initNaverLink() {
        binding.tvToNaver.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(viewModel.detailData.value!!.naverMapUrl))
            startActivity(intent)
        }
    }

    fun setViewPagerHeight(height: Int) {
        val layoutParams = binding.vpMenuReview.layoutParams
        layoutParams.height = height
        binding.vpMenuReview.layoutParams = layoutParams
    }

    private fun changeTopBar() {
        binding.svDetail.viewTreeObserver.addOnScrollChangedListener {
            val scrollY = binding.svDetail.scrollY
            val topBarHeight = binding.detailClTopBar.height
            val tabLayoutPosition = binding.tlMenuReview.top - scrollY

            // 탭 sticky header
            if (tabLayoutPosition <= topBarHeight) {
                binding.tlMenuReview.translationY = (topBarHeight - tabLayoutPosition).toFloat()
            } else {
                binding.tlMenuReview.translationY = 0f
            }

            // 상단 바 색 변환
            if (scrollY >= binding.clStoreInfo.top - binding.detailClTopBar.height) {
                binding.detailClTopBar.setBackgroundColor(Color.WHITE)
            } else {
                binding.detailClTopBar.setBackgroundColor(Color.TRANSPARENT)
            }
        }
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