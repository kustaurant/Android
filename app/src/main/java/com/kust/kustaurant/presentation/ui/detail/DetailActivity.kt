package com.kust.kustaurant.presentation.ui.detail

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import com.kust.kustaurant.R
import com.kust.kustaurant.data.getAccessToken
import com.kust.kustaurant.databinding.ActivityDetailBinding
import com.kust.kustaurant.presentation.common.BaseActivity
import com.kust.kustaurant.presentation.ui.search.SearchActivity
import com.kust.kustaurant.presentation.ui.splash.StartActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailActivity : BaseActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels()
    private lateinit var tierInfoAdapter: DetailTierInfoAdapter
    private var restaurantId = 0
    private var isEvaluated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initEvaluate()
        initBack()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        restaurantId = intent.getIntExtra("restaurantId", 346)

        viewModel.loadDetailData(restaurantId)

        initTierRecyclerView()
        initNaverLink()
        initSearch()
        changeTopBar()
        initFavorite()

        loadData()
    }

    private fun loadData() {
        viewModel.detailData.observe(this) { detailData ->
            binding.tvToNaver.visibility = View.VISIBLE
            if (detailData.partnershipInfo == null){
                binding.detailClAlliance.visibility = View.GONE
            }
            if (detailData.isEvaluated){
                binding.detailIvEvaluateCheck.visibility = View.VISIBLE
                binding.detailTvEvaluate.text = "다시 평가하기"
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
                    initTabView()
                }, 100) // 100ms 후에 실행
            }
        }
    }

    private fun initSearch() {
        binding.detailFlIvSearch.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)

        }
    }


    private fun initFavorite() {
        binding.detailFlFavorite.setOnClickListener {
            checkToken {
                viewModel.toggleFavorite(restaurantId)
            }
        }
    }

    private fun initBack() {
        binding.detailFlIvBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            overridePendingTransition(R.anim.stay_in_place, R.anim.slide_out_right)
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
                binding.detailIvSearch.setImageResource(R.drawable.ic_search)
            } else {
                binding.detailClTopBar.setBackgroundColor(Color.TRANSPARENT)
                binding.detailIvBack.setImageResource(R.drawable.ic_back_white)
                binding.detailIvSearch.setImageResource(R.drawable.ic_search_white)
            }
        }
    }


    private fun initEvaluate() {
        binding.btnEvaluate.setOnClickListener {
            checkToken{
                val intent = Intent(this, EvaluateActivity::class.java)
                intent.putExtra("restaurantId",restaurantId)
                intent.putExtra("isEvaluated", isEvaluated)
                startActivity(intent)
            }
        }
    }

    private fun checkToken(action: () -> Unit) {
        val accessToken = getAccessToken(this)
        if (accessToken == null) {
            val intent = Intent(this, StartActivity::class.java)
            startActivity(intent)
        } else {
            action()
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

    private fun initTabView() {
        viewModel.tabList.observe(this) { tabs ->
            binding.vpMenuReview.adapter = MeReVPAdapter(this, restaurantId)
            TabLayoutMediator(binding.tlMenuReview, binding.vpMenuReview) { tab, position ->
                tab.text = tabs[position]
            }.attach()
        }

        // 리뷰 갱신 코드
//        binding.tlMenuReview.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab) {
//                when (tab.position) {
//                    1 -> viewModel.loadCommentData(restaurantId, "popularity")
//                }
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab) {}
//
//            override fun onTabReselected(tab: TabLayout.Tab) {
//                onTabSelected(tab)
//            }
//        })
    }
}