package com.kust.kustaurant.presentation.ui.home

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.kust.kustaurant.R
import com.kust.kustaurant.data.getAccessToken
import com.kust.kustaurant.data.model.HomeResponse
import com.kust.kustaurant.databinding.FragmentHomeBinding
import com.kust.kustaurant.presentation.ui.detail.DetailActivity
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupRecyclerViews()
        Log.d("token", getAccessToken(requireContext()).toString())

        homeViewModel.homeResponse.observe(viewLifecycleOwner) { response ->
            updateUI(response)
        }

        return binding.root
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
                startActivity(intent)
            }
        })

        topRestaurantadapter.setOnItemClickListener(object : TopRestaurantAdapter.OnItemClickListener{
            override fun onItemClicked(data: RestaurantModel) {
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra("restaurantId", data.restaurantId)
                startActivity(intent)
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
    }
}