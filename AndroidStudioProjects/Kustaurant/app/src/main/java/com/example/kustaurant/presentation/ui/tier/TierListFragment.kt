package com.example.kustaurant.presentation.ui.tier

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kustaurant.R
import com.example.kustaurant.databinding.FragmentTierListBinding
import com.example.kustaurant.presentation.ui.home.RestaurantModel

class TierListFragment : Fragment() {
    private lateinit var binding: FragmentTierListBinding

    // 리사이클러뷰
    lateinit var tierRestaurantList: ArrayList<RestaurantModel>
    lateinit var smallTierRestaurantadapter: SmallTierRestaurantAdapter
    lateinit var wideTierRestaurantadapter: WideTierRestaurantAdapter

    private var isWideView = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTierListBinding.inflate(inflater, container, false)

        // 리사이클러뷰 데이터 초기화
        tierRestaurantList = arrayListOf(
            // 예시 데이터 추가 (실제 데이터로 대체)
            RestaurantModel(702,"홍대돈부리 건대점","일식", "중문~어대","https://search.pstatic.net/common/?autoRotate=true&type=w560_sharpen&src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20210415_47%2F1618456948211vbkJA_JPEG%2FTiiF565NRTRcluKvBW6Wk6tt.jpg", 3,"컴공 10%할인",4.5,true,false),
            RestaurantModel(631, "홍콩포차","술집", "중문~어대",  "https://search.pstatic.net/common/?autoRotate=true&type=w560_sharpen&src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20151008_10%2F1444284591891DP13D_JPEG%2F166955514861358_0.jpeg", 2,"어디대 학생증 제시하면 10%할인",4.5,true,true),
            RestaurantModel(288, "송화산시도삭면 2호점","중식", "건입~중문",  "https://search.pstatic.net/common/?autoRotate=true&type=w560_sharpen&src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20220208_158%2F1644299022190h3uCp_JPEG%2F%25B3%25BB%25BA%25CE.JPG", 1,"제휴사항 없음",5.0, false, false)
        )

        // 어댑터 초기화
        smallTierRestaurantadapter = SmallTierRestaurantAdapter(tierRestaurantList)
        wideTierRestaurantadapter = WideTierRestaurantAdapter(tierRestaurantList)

        binding.recyclerviewTier.adapter = smallTierRestaurantadapter
        binding.recyclerviewTier.layoutManager =  LinearLayoutManager(requireContext())

        binding.tierWideBtn.setOnClickListener {
            changeRecyclerView()
        }
        return binding.root
    }

    private fun changeRecyclerView(){
        if(isWideView){
            binding.recyclerviewTier.adapter = smallTierRestaurantadapter
            binding.tierWideTv.setText(R.string.wide_btn_info)
            binding.tierWideIv.setImageResource(R.drawable.ic_wide_btn)
        }else{
            binding.recyclerviewTier.adapter = wideTierRestaurantadapter
            binding.tierWideTv.setText(R.string.small_btn_info)
            binding.tierWideIv.setImageResource(R.drawable.ic_small_btn)
        }

        isWideView = !isWideView
    }
}