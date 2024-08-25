package com.kust.kustaurant.presentation.ui.detail

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.flexbox.FlexboxLayoutManager
import com.kust.kustaurant.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.kust.kustaurant.R
import com.kust.kustaurant.presentation.ui.home.SpaceDecoration
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels()
    private lateinit var tierInfoAdapter: DetailTierInfoAdapter
    private var isEvaluated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val restaurantId = intent.getIntExtra("restaurantId", 346)
        Log.d("restaurantId", restaurantId.toString())
        viewModel.loadDetailData(restaurantId)

        initBack()
        initTierRecyclerView()
        initNaverLink()
        changeTopBar()
        initFavorite(restaurantId)

        viewModel.detailData.observe(this) { detailData ->
            if (detailData.partnershipInfo == null){
                binding.detailClAlliance.visibility = View.GONE
            }

            if (detailData.isEvaluated){
                binding.detailIvEvaluateCheck.visibility = View.VISIBLE
            }

            binding.detailClFavorite.isSelected = detailData.isFavorite
            isEvaluated = detailData.isEvaluated

            when (detailData.mainTier){
                1 -> binding.detailIvRank.setImageResource(R.drawable.ic_rank_1)
                2 -> binding.detailIvRank.setImageResource(R.drawable.ic_rank_2)
                3 -> binding.detailIvRank.setImageResource(R.drawable.ic_rank_3)
                4 -> binding.detailIvRank.setImageResource(R.drawable.ic_rank_4)
                else -> binding.detailIvRank.setImageResource(R.drawable.ic_rank_all)

            }

            if (detailData != null) {
                Handler(Looper.getMainLooper()).postDelayed({
                    initTabView(restaurantId)
                    initEvaluate(restaurantId)
                }, 100) // 100ms 후에 실행
            }
        }
    }

    private fun initFavorite(restaurantId : Int) {
        binding.detailClFavorite.setOnClickListener {
            viewModel.postFavoriteToggle(restaurantId)
        }
    }

    private fun initBack() {
        binding.detailIvBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun initNaverLink() {
        binding.tvToNaver.setOnClickListener {
            viewModel.detailData.value?.naverMapUrl?.let { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            } ?: run {
                Toast.makeText(this, "데이터를 불러오는 중입니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun setViewPagerHeight(height: Int) {
        binding.vpMenuReview.layoutParams = (binding.vpMenuReview.layoutParams as ViewGroup.LayoutParams).apply {
            this.height = height
        }
    }

    private fun changeTopBar() {
        binding.svDetail.viewTreeObserver.addOnScrollChangedListener {
            val scrollY = binding.svDetail.scrollY
            // 상단 바 색 변환
            if (scrollY >= binding.clStoreInfo.top - binding.detailClTopBar.height) {
                binding.detailClTopBar.setBackgroundColor(Color.WHITE)
                binding.detailIvBack.setImageResource(R.drawable.btn_back)
                binding.detailIvAlarm.setImageResource(R.drawable.ic_alarm)
                binding.detailIvSearch.setImageResource(R.drawable.ic_search)
            } else {
                binding.detailClTopBar.setBackgroundColor(Color.TRANSPARENT)
                binding.detailIvBack.setImageResource(R.drawable.ic_back_white)
                binding.detailIvAlarm.setImageResource(R.drawable.ic_alarm_white)
                binding.detailIvSearch.setImageResource(R.drawable.ic_search_white)
            }
        }
    }


    private fun initEvaluate(restaurantId : Int) {
        binding.btnEvaluate.setOnClickListener {
            val intent = Intent(this, EvaluateActivity::class.java)
            intent.putExtra("restaurantId",restaurantId)
            intent.putExtra("isEvaluated", isEvaluated)
            startActivity(intent)
        }
    }

    private fun initTierRecyclerView() {
        viewModel.tierData.observe(this) { data ->
            tierInfoAdapter = DetailTierInfoAdapter(this, data)
            binding.rvTier.adapter = tierInfoAdapter
            binding.rvTier.layoutManager =
                FlexboxLayoutManager(this)
        }
    }

    private fun initTabView(restaurantId: Int) {
        viewModel.tabList.observe(this) { tabs ->
            binding.vpMenuReview.adapter = MeReVPAdapter(this, restaurantId)
            TabLayoutMediator(binding.tlMenuReview, binding.vpMenuReview) { tab, position ->
                tab.text = tabs[position]
            }.attach()
        }
    }
}