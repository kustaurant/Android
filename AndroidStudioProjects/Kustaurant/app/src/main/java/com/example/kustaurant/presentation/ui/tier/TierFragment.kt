package com.example.kustaurant.presentation.ui.tier

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
import com.example.kustaurant.R
import com.example.kustaurant.databinding.FragmentTierBinding
import com.google.android.material.tabs.TabLayout
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
        binding.viewPager.adapter = pagerAdapter
        binding.viewPager.isUserInputEnabled = false // ViewPager2의 스크롤을 비활성화
    }

    private fun setupTabLayout() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewPager.currentItem = tab.position
                handleTabSelected(tab.position)
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
                handleTabSelected(position)
            }
        })

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("티어표"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("지도"))
    }

    private fun handleTabSelected(position: Int) {
        val layoutParams = binding.selectedCategoryLinearScrollView.layoutParams as ViewGroup.MarginLayoutParams

        if (position == 1) { // 지도 탭
            layoutParams.marginEnd = 0
        } else { // 카테고리 탭
            layoutParams.marginEnd = context?.let { 60.dpToPx(it) }!!
        }

        binding.selectedCategoryLinearScrollView.layoutParams = layoutParams
    }

    fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }

    private fun setupCategoryButton() {
        binding.btnCategory.setOnClickListener {
            hideMainContent()

            val currentTabIndex = binding.viewPager.currentItem
            val fragment = TierCategoryFragment().apply {
                arguments = Bundle().apply {
                    putInt("fromTabIndex", currentTabIndex)
                }
            }

            binding.tabCategoryText.text = "카테고리"
            binding.tabCategoryText.visibility = View.VISIBLE

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun updateCategoryLinearLayout(categories: Set<String>) {
        binding.selectedCategoryLinearLayout.removeAllViews()

        categories.forEach { category ->
            val textView = TextView(context).apply {
                text = category
                setPadding(16, 4, 16, 4) // paddingLeft, paddingTop, paddingRight, paddingBottom
                background = ContextCompat.getDrawable(context, R.drawable.btn_tier_catetory_selected)
                setTextColor(ContextCompat.getColor(context, R.color.signature_1))
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                minWidth = 0
                minHeight = 0
                layoutParams = ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 0, 8, 0) // layout_marginEnd, layout_marginBottom
                }
            }
            binding.selectedCategoryLinearLayout.addView(textView)
        }
    }



    private fun showMainContent() {
        binding.tabCategoryText.visibility = View.GONE
        binding.viewPager.visibility = View.VISIBLE
        binding.tabLayout.visibility = View.VISIBLE
        binding.middleBar.visibility = View.VISIBLE
        pagerAdapter.refreshAllFragments()
    }

    private fun hideMainContent() {
        binding.viewPager.visibility = View.GONE
        binding.tabLayout.visibility = View.GONE
        binding.middleBar.visibility = View.GONE
    }

    private fun observeViewModel() {
        viewModel.selectedCategories.observe(viewLifecycleOwner) { categories ->
            updateCategoryLinearLayout(categories)
        }
    }

    private fun setupBackButton() {
        binding.btnBack.setOnClickListener {
            if (parentFragmentManager.backStackEntryCount > 0) {
                showMainContent()
                parentFragmentManager.popBackStack()
            } else {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    fun navigateToTab(tabIndex: Int) {
        binding.viewPager.currentItem = tabIndex
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}