package com.example.kustaurant.presentation.ui.tier

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
        observeViewModel()

        viewModel.filterApplied.observe(viewLifecycleOwner) { applied ->
            if (applied) {
                showMainContent()
                viewModel.resetFilterApplied()
            }
        }
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
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
            }
        })

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("티어표"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("지도"))
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
                setPadding(16, 8, 16, 8)
                background = ContextCompat.getDrawable(context, R.drawable.btn_tier_category_apply_active)
                setTextColor(ContextCompat.getColor(context, R.color.signature_1))
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                layoutParams = ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(8, 0, 8, 0)
                }
            }
            binding.selectedCategoryLinearLayout.addView(textView)
        }
    }


    private fun showMainContent() {
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}