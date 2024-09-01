package com.kust.kustaurant.presentation.ui.tier

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.kust.kustaurant.R
import com.kust.kustaurant.databinding.FragmentTierBinding
import com.google.android.material.tabs.TabLayout
import com.kust.kustaurant.MainActivity
import com.kust.kustaurant.presentation.ui.home.HomeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TierFragment : Fragment() {
    private var _binding: FragmentTierBinding? = null
    private val viewModel: TierViewModel by activityViewModels()
    val binding get() = _binding!!

    private lateinit var pagerAdapter: TierPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTierBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        setupTabLayout()
        setupCategoryButton()
        setupBackButton()
        observeViewModel()

        viewModel.filterApplied.observe(viewLifecycleOwner) { applied ->
            if (applied) {
                showMainContent()
                viewModel.resetFilterApplied()
            }
        }
        updateCategoryLinearLayout(setOf("전체"))
    }

    private fun setupViewPager() {
        pagerAdapter = TierPagerAdapter(this)
        binding.tierViewPager.adapter = pagerAdapter
        binding.tierViewPager.isUserInputEnabled = false // ViewPager2의 스크롤을 비활성화
    }

    private fun setupTabLayout() {
        binding.tierTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.tierViewPager.currentItem = tab.position
                handleTabSelected(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        binding.tierViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.tierTabLayout.selectTab(binding.tierTabLayout.getTabAt(position))
                handleTabSelected(position)
            }
        })

        binding.tierTabLayout.addTab(binding.tierTabLayout.newTab().setText("티어표"))
        binding.tierTabLayout.addTab(binding.tierTabLayout.newTab().setText("지도"))
    }

    private fun handleTabSelected(position: Int) {
        val layoutParams =
            binding.tierSvSelectedCategory.layoutParams as ViewGroup.MarginLayoutParams

        if (position == 1) { // 지도 탭
            layoutParams.marginEnd = 0
        } else { // 카테고리 탭
            layoutParams.marginEnd = context?.let { 60.dpToPx(it) }!!
        }

        binding.tierSvSelectedCategory.layoutParams = layoutParams
    }

    fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }

    private fun setupCategoryButton() {
        binding.tierIvCategoryBtn.setOnClickListener {
            hideMainContent()

            val currentTabIndex = binding.tierViewPager.currentItem
            val fragment = TierCategoryFragment().apply {
                arguments = Bundle().apply {
                    putInt("fromTabIndex", currentTabIndex)
                }
            }

            binding.tierTvCategoryText.text = "카테고리"
            binding.tierTvCategoryText.visibility = View.VISIBLE

            parentFragmentManager.beginTransaction()
                .replace(R.id.tier_fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun updateCategoryLinearLayout(categories: Set<String>) {
        binding.selectedCategoryLinearLayout.removeAllViews()

        categories.forEach { category ->
            val textView = TextView(context).apply {
                text = category
                setTextColor(ContextCompat.getColor(context, R.color.signature_1))
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                background =
                    ContextCompat.getDrawable(context, R.drawable.btn_tier_catetory_selected)

                val paddingHorizontal = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 13f, resources.displayMetrics
                ).toInt()
                val paddingVertical = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 4f, resources.displayMetrics
                ).toInt()
                setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical)

                layoutParams = ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    val marginEndPx = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 7f, resources.displayMetrics
                    ).toInt()
                    marginEnd = marginEndPx
                }
            }
            binding.selectedCategoryLinearLayout.addView(textView)
        }
    }

    fun showMainContent() {
        binding.tierTvCategoryText.visibility = View.GONE
        binding.tierViewPager.visibility = View.VISIBLE
        binding.tierTabLayout.visibility = View.VISIBLE
        binding.tierClMiddleBar.visibility = View.VISIBLE
        binding.tierViewPager.post {
            pagerAdapter.refreshAllFragments()
        }
    }

    private fun hideMainContent() {
        binding.tierViewPager.visibility = View.GONE
        binding.tierTabLayout.visibility = View.GONE
        binding.tierClMiddleBar.visibility = View.GONE
    }

    private fun observeViewModel() {
        viewModel.selectedCategories.observe(viewLifecycleOwner) { categories ->
            updateCategoryLinearLayout(categories)
        }
    }

    private fun setupBackButton() {
        binding.btnBack.setOnClickListener {
            // 백스택에 프래그먼트가 있는지 확인
            if (parentFragmentManager.backStackEntryCount > 0) {
                // 백스택에서 최근의 프래그먼트가 TierCategoryFragment인지 확인
                val currentFragment = parentFragmentManager.findFragmentById(R.id.tier_fragment_container)
                if (currentFragment is TierCategoryFragment) {
                    // TierCategoryFragment가 있다면 먼저 뷰페이저를 보여줌
                    showMainContent()
                    parentFragmentManager.popBackStack() // TierCategoryFragment 제거
                } else {
                    // 그렇지 않으면 HomeFragment로 이동
                    (requireActivity() as? MainActivity)?.let { mainActivity ->
                        mainActivity.supportFragmentManager.beginTransaction()
                            .replace(R.id.main_frm, HomeFragment())
                            .commit()
                        mainActivity.binding.mainNavigation.selectedItemId = R.id.menu_home
                    }
                }
            } else {
                // 백스택에 프래그먼트가 없는 경우
                (requireActivity() as? MainActivity)?.let { mainActivity ->
                    mainActivity.supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commit()
                    mainActivity.binding.mainNavigation.selectedItemId = R.id.menu_home
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}