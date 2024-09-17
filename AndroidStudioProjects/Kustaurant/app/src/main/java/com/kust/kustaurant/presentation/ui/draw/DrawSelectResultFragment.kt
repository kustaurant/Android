package com.kust.kustaurant.presentation.ui.draw

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.kust.kustaurant.R
import com.kust.kustaurant.data.model.DrawRestaurantData
import com.kust.kustaurant.databinding.FragmentDrawSelectResultBinding
import com.kust.kustaurant.presentation.ui.detail.DetailActivity
import kotlin.math.abs
import kotlin.math.floor

class DrawSelectResultFragment : Fragment() {
    private var _binding: FragmentDrawSelectResultBinding? = null
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: DrawSelectResultAdapter
    private var restaurantList = mutableListOf<DrawRestaurantData>()
    private val viewModel: DrawViewModel by activityViewModels()
    private var handler: Handler? = null
    private var runnable: Runnable? = null
    val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDrawSelectResultBinding.inflate(inflater, container, false)
        setupViewPager()
        loadRestaurants()
        setupButton()
        setupObservers()
        return binding.root
    }

    private fun setupObservers() {
        viewModel.selectedIndex.observe(viewLifecycleOwner) { index ->
            if (index != null && index != RecyclerView.NO_POSITION) {
                viewPager.setCurrentItem(index, true)
                adapter.highlightItem(index)
            }
        }

        viewModel.selectedRestaurant.observe(viewLifecycleOwner) { selected ->
            if (_binding == null) {
                _binding = FragmentDrawSelectResultBinding.inflate(layoutInflater)
            }

            displaySelectedRestaurantInfo(selected)
        }
    }


    @SuppressLint("SetTextI18n")
    private fun displaySelectedRestaurantInfo(restaurant: DrawRestaurantData) {
        binding.drawTvRestaurantName.text = restaurant.restaurantName
        binding.drawTvRestaurantMenu.text = restaurant.restaurantCuisine

        binding.drawViewPager.setOnClickListener {
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("restaurantId",viewModel.selectedRestaurant.value!!.restaurantId)
            startActivity(intent)
        }
    }

    private fun setupViewPager() {
        viewPager = binding.drawViewPager
        adapter = DrawSelectResultAdapter(restaurantList)
        viewPager.adapter = adapter
        viewPager.isUserInputEnabled = false // 스크롤 비활성화
        viewPager.offscreenPageLimit = 1
        val pageTransformer = ViewPager2.PageTransformer { page, position ->
            val absPosition = abs(position)
            page.apply {
                rotationY = -30 * position
                scaleX = 0.8f + (1 - absPosition) * 0.2f
                scaleY = scaleX
            }
        }
        viewPager.setPageTransformer(pageTransformer)
    }

    private fun updateStarRating(score: Double) {
        val fullStars = score.toInt()
        val hasHalfStar = score % 1 >= 0.5

        val starIds = listOf(R.id.draw_iv_star_1, R.id.draw_iv_star_2, R.id.draw_iv_star_3, R.id.draw_iv_star_4, R.id.draw_iv_star_5)
        starIds.forEachIndexed { index, starId ->
            val star = binding.root.findViewById<ImageView>(starId)
            when {
                index < fullStars -> star.setImageResource(R.drawable.ic_star_full)
                index == fullStars && hasHalfStar -> star.setImageResource(R.drawable.ic_star_half)
                else -> star.setImageResource(R.drawable.ic_star_empty)
            }
        }
    }

    private fun setupButton() {
        val drawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setStroke(1, ContextCompat.getColor(requireContext(), R.color.signature_1))
            cornerRadius = 100f
            setColor(ContextCompat.getColor(requireContext(), R.color.white))
        }

        binding.drawBtnCategoryReset.background = drawable

        binding.drawBtnRetry.setOnClickListener {
            binding.drawTvRestaurantScore.text = ""
            binding.drawTvRestaurantPartnershipInfo.text = ""
            binding.drawLlScoreImgGroup.visibility = View.GONE

            loadRestaurants()
        }

        binding.drawBtnCategoryReset.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.draw_fragment_container, DrawSelectCategoryFragment())
                .addToBackStack(null)
                .commit()
        }
    }
    private fun loadRestaurants() {
        viewModel.drawRestaurants()

        viewModel.drawList.observe(viewLifecycleOwner) { list ->
            restaurantList.clear()
            restaurantList.addAll(list)
            adapter.notifyDataSetChanged()

            // 애니메이션 시작
            startAnimation()
        }
    }

    private fun highlightCenterImage() {
        val centerPosition = viewPager.currentItem
        adapter.highlightItem(centerPosition)

    }

    private fun startAnimation() {
        disableButtons() // 애니메이션 시작 시 버튼 비활성화
        // 텍스트뷰의 alpha 애니메이션 추가
        val textView = binding.drawSelectedTvClickableInfo
        textView.alpha = 0f

        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            var currentPage = adapter.itemCount - 1
            override fun run() {
                // Fragment가 아직 활성 상태인지 확인
                if (!isAdded || view == null || _binding == null) {
                    handler?.removeCallbacks(this) // 핸들러에서 콜백 제거
                    return
                }

                if (currentPage >= 0) {
                    viewPager.setCurrentItem(currentPage--, true)
                    handler?.postDelayed(this, 1700L / adapter.itemCount)
                    displaySelectedRestaurantInfo(restaurantList[currentPage + 1])
                } else {
                    // 애니메이션 종료 후, 중앙에 선택된 음식점 배치
                    viewModel.selectedRestaurant.value?.let { selected ->
                        val selectedIndex = restaurantList.indexOf(selected)
                        viewPager.setCurrentItem(selectedIndex, true)
                        displaySelectedRestaurantInfo(selected)

                        val roundedScore = selected.restaurantScore?.let {
                            floor(it * 2) / 2
                        } ?: "평가 없음"

                        binding.drawTvRestaurantScore.text = roundedScore.toString()
                        binding.drawTvRestaurantPartnershipInfo.text = selected.partnershipInfo?: "제휴 해당사항 없음"

                        updateStarRating(selected.restaurantScore?: 0.0)
                        binding.drawLlScoreImgGroup.visibility = View.VISIBLE

                        highlightCenterImage()
                        enableButtons()

                        val fadeIn = ObjectAnimator.ofFloat(textView, "alpha", 0f, 1f)
                        fadeIn.duration = 500
                        fadeIn.start()
                    }
                }
            }
        }
        handler?.post(runnable!!)
    }

    private fun disableButtons() {
        binding.drawBtnCategoryReset.isClickable = false
        binding.drawBtnRetry.isClickable = false
        binding.drawBtnCategoryReset.alpha = 0.5f
        binding.drawBtnRetry.alpha = 0.5f
    }

    private fun enableButtons() {
        binding.drawBtnCategoryReset.isClickable = true
        binding.drawBtnRetry.isClickable = true
        binding.drawBtnCategoryReset.alpha = 1.0f
        binding.drawBtnRetry.alpha = 1.0f
    }

    override fun onDestroyView() {
        super.onDestroyView()

        handler?.removeCallbacks(runnable!!)
        handler = null
        runnable = null
        _binding = null
    }
}