package com.kust.kustaurant.presentation.ui.home

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.kust.kustaurant.R
import com.kust.kustaurant.data.getAccessToken
import com.kust.kustaurant.data.model.HomeResponse
import com.kust.kustaurant.databinding.FragmentHomeBinding
import com.kust.kustaurant.presentation.ui.detail.DetailActivity
import com.kust.kustaurant.presentation.ui.search.SearchActivity
import com.kust.kustaurant.presentation.util.TouchExtension
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var meRestaurantList: ArrayList<RestaurantModel>
    private lateinit var topRestaurantList: ArrayList<RestaurantModel>
    private lateinit var meRestaurantadapter: MeRestaurantAdapter
    private lateinit var topRestaurantadapter: TopRestaurantAdapter
    private val homeViewModel: HomeViewModel by viewModels()

    private var imageUrls = listOf<String>()
    private lateinit var autoScrollHandler: Handler
    private lateinit var autoScrollRunnable: Runnable
    private var currentPage = 0
    private val delayMillis = 5000L // 5초

    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupCategoryRV()
        autoScrollHandler = Handler(Looper.getMainLooper())
        autoScrollRunnable = Runnable {
            // 아무 것도 안 함, 실제 로직은 setupViewPager에서 설정
        }

        setupRecyclerViews()
        Log.d("token", getAccessToken(requireContext()).toString())

        homeViewModel.homeResponse.observe(viewLifecycleOwner) { response ->
            updateUI(response)
        }

        binding.btnSearch.setOnClickListener {
            val intent = Intent(requireContext(), SearchActivity::class.java)
            startActivity(intent)
        }

        initTouchExtension()

        return binding.root
    }

    private fun initTouchExtension() {
        TouchExtension.expandTouchArea(binding.homeTopbar, binding.btnSearch, 40)
    }


    private fun setupCategoryRV(){
        val categoryList = listOf(
            CategoryItem(R.drawable.img_category_all, "전체"),
            CategoryItem(R.drawable.img_category_korea, "한식"),
            CategoryItem(R.drawable.img_category_japan, "일식"),
            CategoryItem(R.drawable.img_category_china, "중식"),
            CategoryItem(R.drawable.img_category_western, "양식"),
            CategoryItem(R.drawable.img_category_asian, "아시안"),
            CategoryItem(R.drawable.img_category_meat, "고기"),
            CategoryItem(R.drawable.img_category_seafood, "해산물"),
            CategoryItem(R.drawable.img_category_chicken, "치킨"),
            CategoryItem(R.drawable.img_category_hamburger_pizza, "햄버거/피자"),
            CategoryItem(R.drawable.img_category_tteokbokki, "분식"),
            CategoryItem(R.drawable.img_category_beer, "술집"),
            CategoryItem(R.drawable.img_category_cafe, "카페/디저트"),
            CategoryItem(R.drawable.img_category_bakery, "베이커리"),
            CategoryItem(R.drawable.img_category_salad, "샐러드"),
            CategoryItem(R.drawable.img_category_benefit, "제휴업체"),
        )

        categoryAdapter = CategoryAdapter(categoryList)
        binding.categoryRecyclerView.apply {
            val spanCount = 4
            val spacing = 10
            val spacingInPx = context.dpToPx(spacing) // dp를 px로 변환

            addItemDecoration(CategorySpaceDecoration(spanCount, spacingInPx))
            adapter = categoryAdapter
        }
    }

    fun Context.dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }

    private fun setupRecyclerViews() {
        meRestaurantList = arrayListOf()
        topRestaurantList = arrayListOf()

        meRestaurantadapter = MeRestaurantAdapter(meRestaurantList)
        topRestaurantadapter = TopRestaurantAdapter(topRestaurantList)

        binding.homeMERv.adapter = meRestaurantadapter
        binding.homeMERv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.homeTOPRv.adapter = topRestaurantadapter
        binding.homeTOPRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // Horizontal Margin 적용
        val size = resources.getDimensionPixelSize(R.dimen.MY_SIZE)
        val m_size = resources.getDimensionPixelSize(R.dimen.MY_EDGE_MARGIN)
        val deco = SpaceDecoration(size, m_size)
        binding.homeMERv.addItemDecoration(deco)
        binding.homeTOPRv.addItemDecoration(deco)

        meRestaurantadapter.setOnItemClickListener(object : MeRestaurantAdapter.OnItemClickListener{
            override fun onItemClicked(data: RestaurantModel) {
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra("restaurantId", data.restaurantId)
                val options = ActivityOptions.makeCustomAnimation(requireContext(), R.anim.slide_in_right, R.anim.stay_in_place)
                startActivity(intent, options.toBundle())
            }
        })

        topRestaurantadapter.setOnItemClickListener(object : TopRestaurantAdapter.OnItemClickListener{
            override fun onItemClicked(data: RestaurantModel) {
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra("restaurantId", data.restaurantId)
                val options = ActivityOptions.makeCustomAnimation(requireContext(), R.anim.slide_in_right, R.anim.stay_in_place)
                startActivity(intent, options.toBundle())
            }
        })
    }

    private fun updateUI(response: HomeResponse) {
        imageUrls = response.photoUrls

        meRestaurantList.clear()
        meRestaurantList.addAll(response.restaurantsForMe.map { restaurantResponse ->
            RestaurantModel(
                restaurantResponse.restaurantId,
                restaurantResponse.restaurantName,
                restaurantResponse.restaurantCuisine,
                restaurantResponse.restaurantPosition,
                restaurantResponse.restaurantImgUrl,
                restaurantResponse.mainTier,
                restaurantResponse.partnershipInfo ?: "해당사항 없음",
                restaurantResponse.restaurantScore?.toDouble() ?: 0.0,
                restaurantResponse.isEvaluated,
                restaurantResponse.isFavorite
            )
        })

        topRestaurantList.clear()
        topRestaurantList.addAll(response.topRestaurantsByRating.map { restaurantResponse ->
            RestaurantModel(
                restaurantResponse.restaurantId,
                restaurantResponse.restaurantName,
                restaurantResponse.restaurantCuisine,
                restaurantResponse.restaurantPosition,
                restaurantResponse.restaurantImgUrl,
                restaurantResponse.mainTier,
                restaurantResponse.partnershipInfo ?: "해당사항 없음",
                restaurantResponse.restaurantScore?.toDouble() ?: 0.0,
                restaurantResponse.isEvaluated,
                restaurantResponse.isFavorite
            )
        })

        meRestaurantadapter.notifyDataSetChanged()
        topRestaurantadapter.notifyDataSetChanged()

        setupViewPager()
    }

    private fun setupViewPager() {
        val adapter = HomeAdBannerPagerAdapter(imageUrls)
        binding.homeAdBanner.adapter = adapter

        binding.homeAdBanner.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentPage = position + 1
                val totalPageNumber = adapter.itemCount
                binding.homeAdBannerNumber.text = "$currentPage/$totalPageNumber"
            }
        })

        // 자동 스크롤 설정
        autoScrollHandler = Handler(Looper.getMainLooper())
        autoScrollRunnable = object : Runnable {
            override fun run() {
                if (binding.homeAdBanner.currentItem < adapter.itemCount - 1) {
                    binding.homeAdBanner.currentItem += 1
                } else {
                    binding.homeAdBanner.currentItem = 0 // 마지막 이미지 후 첫 번째 이미지로 돌아감
                }
                autoScrollHandler.postDelayed(this, delayMillis)
            }
        }
        autoScrollHandler.postDelayed(autoScrollRunnable, delayMillis)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // View가 파괴될 때 Handler를 정리합니다.
        autoScrollHandler.removeCallbacks(autoScrollRunnable)

        // View가 파괴될 때 Handler를 정리합니다.
        if (::autoScrollRunnable.isInitialized && ::autoScrollHandler.isInitialized) {
            autoScrollHandler.removeCallbacks(autoScrollRunnable)
        }
    }
}