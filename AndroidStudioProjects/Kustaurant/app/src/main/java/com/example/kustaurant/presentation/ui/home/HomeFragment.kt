package com.example.kustaurant.presentation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.kustaurant.R
import com.example.kustaurant.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private var selectedColor: Int = 0
    private var defaultColor: Int = 0
    lateinit var meRestaurantList: ArrayList<RestaurantModel>
    lateinit var topRestaurantList: ArrayList<RestaurantModel>
    lateinit var meRestaurantadapter: MeRestaurantAdapter
    lateinit var topRestaurantadapter: TopRestaurantAdapter

    // 예시 이미지
    private val imageUrls = listOf(
        "https://search.pstatic.net/common/?autoRotate=true&type=w560_sharpen&src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20221219_73%2F1671415873694AWTMq_JPEG%2FDSC04440.jpg",
        "https://img.freepik.com/free-photo/cheesy-tokbokki-korean-traditional-food-on-black-board-background-lunch-dish_1150-42992.jpg",
        "https://search.pstatic.net/common/?autoRotate=true&type=w560_sharpen&src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20221219_73%2F1671415873694AWTMq_JPEG%2FDSC04440.jpg",
        "https://search.pstatic.net/common/?autoRotate=true&type=w560_sharpen&src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20221219_73%2F1671415873694AWTMq_JPEG%2FDSC04440.jpg",
        "https://search.pstatic.net/common/?autoRotate=true&type=w560_sharpen&src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20221219_73%2F1671415873694AWTMq_JPEG%2FDSC04440.jpg"
    )
    private var selectedIndex = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        selectedColor = ContextCompat.getColor(requireContext(), R.color.cement_4)
        defaultColor = ContextCompat.getColor(requireContext(), R.color.cement_3)
        setupViewPager()

        // 데이터 초기화
        meRestaurantList = arrayListOf(
            // 예시 데이터 추가 (실제 데이터로 대체)
            RestaurantModel(702,"홍대돈부리 건대점","일식", "중문~어대","https://search.pstatic.net/common/?autoRotate=true&type=w560_sharpen&src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20210415_47%2F1618456948211vbkJA_JPEG%2FTiiF565NRTRcluKvBW6Wk6tt.jpg", 3,"컴공 10%할인", 4.5,true, true),
            RestaurantModel(631, "홍콩포차","술집", "중문~어대",  "https://search.pstatic.net/common/?autoRotate=true&type=w560_sharpen&src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20151008_10%2F1444284591891DP13D_JPEG%2F166955514861358_0.jpeg", 1,"제휴사항 없음",5.0,true, true)
        )
        topRestaurantList = arrayListOf(
            // 예시 데이터 추가 (실제 데이터로 대체)
            RestaurantModel(702,"홍대돈부리 건대점","일식", "중문~어대","https://search.pstatic.net/common/?autoRotate=true&type=w560_sharpen&src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20210415_47%2F1618456948211vbkJA_JPEG%2FTiiF565NRTRcluKvBW6Wk6tt.jpg", 3,"컴공 10%할인",4.5,true, true),
            RestaurantModel(631, "홍콩포차","술집", "중문~어대",  "https://search.pstatic.net/common/?autoRotate=true&type=w560_sharpen&src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20151008_10%2F1444284591891DP13D_JPEG%2F166955514861358_0.jpeg", 2,"제휴사항 없음",4.5,true, true),
            RestaurantModel(288, "송화산시도삭면 2호점","중식", "건입~중문",  "https://search.pstatic.net/common/?autoRotate=true&type=w560_sharpen&src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20220208_158%2F1644299022190h3uCp_JPEG%2F%25B3%25BB%25BA%25CE.JPG", 1,"제휴사항 없음",5.0,true, true)
        )

        // 어댑터 초기화
        meRestaurantadapter = MeRestaurantAdapter(meRestaurantList)
        topRestaurantadapter = TopRestaurantAdapter(topRestaurantList)

        binding.homeMERv.adapter = meRestaurantadapter
        binding.homeMERv.layoutManager =  LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.homeTOPRv.adapter = topRestaurantadapter
        binding.homeTOPRv.layoutManager =  LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)

        // Horizontal Margin 적용
        val size = resources.getDimensionPixelSize(R.dimen.MY_SIZE)
        val deco = SpaceDecoration(size)
        binding.homeMERv.addItemDecoration(deco)
        binding.homeTOPRv.addItemDecoration(deco)

        return binding.root
    }

    private fun setupViewPager() {
        val adapter = HomeAdBannerPagerAdapter(imageUrls)
        binding.homeAdBanner.adapter = adapter

        binding.homeAdBanner.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }
        })
    }
}