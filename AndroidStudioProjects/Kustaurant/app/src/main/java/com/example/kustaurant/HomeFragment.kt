package com.example.kustaurant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.kustaurant.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private var selectedColor: Int = 0
    private var defaultColor: Int = 0

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
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        selectedColor = ContextCompat.getColor(requireContext(),R.color.main_menu_item_gray)
        defaultColor = ContextCompat.getColor(requireContext(),R.color.btn_gray)
        setupButtons()
        loadImage(selectedIndex)
        // 예시 이미지 삽입
        Glide.with(this)
            .load(imageUrls[1])
            .into(binding.homeTOPCl1Imgurl)
        Glide.with(this)
            .load(imageUrls[1])
            .into(binding.homeTOPCl2Imgurl)
        Glide.with(this)
            .load(imageUrls[2])
            .into(binding.homeMECl1Imgurl)

        return binding.root
    }

    private fun setupButtons(){
        val buttons = listOf(
            binding.homeBtn1,
            binding.homeBtn2,
            binding.homeBtn3,
            binding.homeBtn4,
            binding.homeBtn5
        )
        
        buttons.forEachIndexed { index, button ->
            button.setOnClickListener{
                updateButtonState(index)
                loadImage(index)
            }
        }

        updateButtonState(selectedIndex)
    }

    private fun updateButtonState(selectedIndex: Int){
        val buttons = listOf(
            binding.homeBtn1,
            binding.homeBtn2,
            binding.homeBtn3,
            binding.homeBtn4,
            binding.homeBtn5
        )

        buttons.forEachIndexed { index, button ->
            if (index == selectedIndex){
                button.layoutParams.width = dpToPx(16)
                button.layoutParams.height = dpToPx(16)
                button.isSelected = true
                button.requestLayout()
            }else{
                button.layoutParams.width = dpToPx(12)
                button.layoutParams.height = dpToPx(12)
                button.isSelected = false
                button.requestLayout()
            }
        }
    }

    private fun loadImage(index: Int){
        val imageUrl = imageUrls[index]
        Glide.with(this)
            .load(imageUrl)
            .into(binding.homeAdBanner)
    }

    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }
}