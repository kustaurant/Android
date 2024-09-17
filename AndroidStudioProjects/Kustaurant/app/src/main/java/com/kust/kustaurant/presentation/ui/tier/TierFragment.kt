package com.kust.kustaurant.presentation.ui.tier

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.kust.kustaurant.presentation.ui.search.SearchActivity
import com.kust.kustaurant.presentation.util.TouchExtension
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


        Log.d("TierFragment", "ViewModel instance: ${viewModel.hashCode()}")

        if ((viewModel.selectedMenus.value ?: emptySet()) == setOf("") &&
            (viewModel.selectedSituations.value ?: emptySet()) == setOf("") &&
            (viewModel.selectedLocations.value ?: emptySet()) == setOf("")
        ) {
            viewModel.applyFilters(setOf("전체"), setOf("전체"), setOf("전체"), 0)
        }

        binding.tierIvSearch.setOnClickListener {
            val intent = Intent(requireContext(), SearchActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        setupTabLayout()
        setupCategoryButton()
        setupBackButton()
        observeViewModel()
        initTouchExtension()

        viewModel.filterApplied.observe(viewLifecycleOwner) { applied ->
            if (applied) {
                showMainContent()
                viewModel.resetFilterApplied()
            }
        }
        updateCategoryLinearLayout(setOf("전체"))
    }


    private fun initTouchExtension() {
        TouchExtension.expandTouchArea(binding.topBar, binding.btnBack, 40)
        TouchExtension.expandTouchArea(binding.topBar, binding.tierIvSearch, 40)
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
            val selectedMenus = viewModel.selectedMenus.value ?: emptySet()
            val selectedSituations = viewModel.selectedSituations.value ?: emptySet()
            val selectedLocations = viewModel.selectedLocations.value ?: emptySet()

            Log.e("TierCategoryActivity", "Selected Menus: $selectedMenus")
            Log.e("TierCategoryActivity", "Selected Situations: $selectedSituations")
            Log.e("TierCategoryActivity", "Selected Locations: $selectedLocations")

            val intent = Intent(requireContext(), TierCategoryActivity::class.java).apply {
                putStringArrayListExtra("selectedMenus", ArrayList(selectedMenus))
                putStringArrayListExtra("selectedSituations", ArrayList(selectedSituations))
                putStringArrayListExtra("selectedLocations", ArrayList(selectedLocations))
            }
            startActivityForResult(intent, CATEGORY_UPDATE_REQUEST_CODE)
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
                    TypedValue.COMPLEX_UNIT_DIP, 5.9f, resources.displayMetrics
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

    private fun observeViewModel() {
        viewModel.selectedCategories.observe(viewLifecycleOwner) { categories ->
            updateCategoryLinearLayout(categories)
        }
    }

    private fun setupBackButton() {
        binding.btnBack.setOnClickListener {
            (requireActivity() as? MainActivity)?.let { mainActivity ->
                mainActivity.supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, HomeFragment())
                    .commit()
                mainActivity.binding.mainNavigation.selectedItemId = R.id.menu_home
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CATEGORY_UPDATE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.let { intent ->
                val selectedMenus = intent.getStringArrayListExtra("selectedMenus")?.toSet() ?: emptySet()
                val selectedSituations = intent.getStringArrayListExtra("selectedSituations")?.toSet() ?: emptySet()
                val selectedLocations = intent.getStringArrayListExtra("selectedLocations")?.toSet() ?: emptySet()
                val fromTabIndex = intent.getIntExtra("fromTabIndex", 0)

                viewModel.applyFilters(selectedMenus, selectedSituations, selectedLocations, fromTabIndex)
            }
        }
    }

    companion object {
        private const val CATEGORY_UPDATE_REQUEST_CODE = 1220
    }
}