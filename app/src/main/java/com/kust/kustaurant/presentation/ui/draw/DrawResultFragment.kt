package com.kust.kustaurant.presentation.ui.draw

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.kust.kustaurant.R
import com.kust.kustaurant.data.model.DrawRestaurantData
import com.kust.kustaurant.databinding.FragmentDrawResultBinding
import com.kust.kustaurant.presentation.ui.detail.DetailActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.floor

class DrawResultFragment : Fragment() {
    private var _binding: FragmentDrawResultBinding? = null
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: DrawSelectResultAdapter
    private var restaurantList = mutableListOf<DrawRestaurantData>()
    private val viewModel: DrawViewModel by activityViewModels()
    val binding get() = _binding!!
    private val animationScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDrawResultBinding.inflate(inflater, container, false)
        setupViewPager()
        loadRestaurants()
        setupButton()
        setupObservers()
        return binding.root
    }

    private fun setupObservers() {
        viewModel.selectedRestaurant.observe(viewLifecycleOwner) { selected ->
            if (_binding == null) {
                _binding = FragmentDrawResultBinding.inflate(layoutInflater)
            }

            displaySelectedRestaurantInfo(selected)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun displaySelectedRestaurantInfo(restaurant: DrawRestaurantData) {
        binding.drawTvRestaurantName.text = restaurant.restaurantName
        binding.drawTvRestaurantMenu.text = restaurant.restaurantCuisine
    }

    private fun setupViewPager() {
        val viewPagerHeight = calculateViewHeight(267f) // 동적 높이 계산

        viewPager = binding.drawViewPager
        adapter = DrawSelectResultAdapter(restaurantList)
        viewPager.adapter = adapter
        viewPager.isUserInputEnabled = false // 스크롤 비활성화
        viewPager.offscreenPageLimit = 1

        // ViewPager의 높이를 동적으로 설정
        viewPager.layoutParams = viewPager.layoutParams.apply {
            height = viewPagerHeight
        }

        // 페이지 전환 애니메이션 설정
        val pageTransformer = ViewPager2.PageTransformer { page, position ->
            page.apply {
                translationX = 20 * position
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
            binding.drawIvSelect.visibility = View.INVISIBLE
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
            adapter.notifyItemRangeInserted(0, list.size)

            startAnimation()
        }
    }

    private fun startAnimation() = animationScope.launch {
        disableButtons()
        val textView = binding.drawSelectedTvClickableInfo
        textView.alpha = 0f

        var currentPage = adapter.itemCount - 1
        while (currentPage >= 0) {
            if (!isAdded) return@launch // Fragment가 아직 활성 상태인지 확인

            viewPager.setCurrentItem(currentPage, true)
            delay(1700L / adapter.itemCount)
            displaySelectedRestaurantInfo(restaurantList[currentPage])
            currentPage--
        }

        // 애니메이션 종료 후, 중앙에 선택된 음식점 배치 및 이미지 표시
        viewModel.selectedRestaurant.value?.let { selected ->
            val selectedIndex = restaurantList.indexOf(selected)
            viewPager.setCurrentItem(selectedIndex, true)

            delay(500L)
            displaySelectedRestaurantInfo(selected)
            val roundedScore = selected.restaurantScore?.let { floor(it * 2) / 2 } ?: "평가 없음"
            binding.drawTvRestaurantScore.text = roundedScore.toString()
            binding.drawTvRestaurantPartnershipInfo.text = selected.partnershipInfo ?: "제휴 해당사항 없음"
            updateStarRating(selected.restaurantScore ?: 0.0)
            binding.drawLlScoreImgGroup.visibility = View.VISIBLE

            displaySelectedRestaurantImage(selected)
            enableButtons()

            val fadeIn = ObjectAnimator.ofFloat(textView, "alpha", 0f, 1f)
            fadeIn.duration = 1000
            fadeIn.start()
        }
    }

    private fun displaySelectedRestaurantImage(restaurant: DrawRestaurantData) {


        Glide.with(this)
            .load(restaurant.restaurantImgUrl)
            .transform(CenterCrop(), RoundedCorners(78))
            .into(binding.drawIvSelect)

        binding.drawIvSelect.alpha = 0f
        binding.drawIvSelect.visibility = View.VISIBLE

        binding.drawIvSelect.animate()
            .alpha(1f)
            .setDuration(1000)
            .setListener(null)
            .start()
    }



    private fun disableButtons() {
        binding.drawBtnCategoryReset.isClickable = false
        binding.drawBtnRetry.isClickable = false
        binding.drawBtnCategoryReset.alpha = 0.5f
        binding.drawBtnRetry.alpha = 0.5f

        binding.drawViewPager.setOnClickListener(null)
    }

    private fun enableButtons() {
        binding.drawBtnCategoryReset.isClickable = true
        binding.drawBtnRetry.isClickable = true
        binding.drawBtnCategoryReset.alpha = 1.0f
        binding.drawBtnRetry.alpha = 1.0f

        binding.drawViewPager.setOnClickListener {
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("restaurantId",viewModel.selectedRestaurant.value!!.restaurantId)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        animationScope.cancel()
        _binding = null
    }

    private fun calculateViewHeight(height : Float): Int {
        val screenDensity = Resources.getSystem().displayMetrics.density
        return (height * screenDensity).toInt()
    }
}