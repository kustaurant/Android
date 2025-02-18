package com.kust.kustaurant.presentation.ui.tier

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.kust.kustaurant.R
import com.kust.kustaurant.databinding.FragmentTierBinding
import com.google.android.material.tabs.TabLayout
import com.kust.kustaurant.MainActivity
import com.kust.kustaurant.data.getAccessToken
import com.kust.kustaurant.presentation.ui.home.HomeFragment
import com.kust.kustaurant.presentation.ui.search.SearchActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TierFragment : Fragment() {
    private var _binding: FragmentTierBinding? = null
    private val viewModel: TierViewModel by activityViewModels()
    private val binding get() = _binding!!
    private lateinit var pagerAdapter: TierPagerAdapter

    private val tierCategoryActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { data ->
                handleCategoryActivityResult(data)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTierBinding.inflate(inflater, container, false)

        initializeFilters()
        setupButtons()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        setupTabLayout()
        observeViewModel()
    }

    private fun initializeFilters() {
        if ((viewModel.selectedMenus.value ?: emptySet()) == setOf("") &&
            (viewModel.selectedSituations.value ?: emptySet()) == setOf("") &&
            (viewModel.selectedLocations.value ?: emptySet()) == setOf("")
        ) {
            viewModel.setCategory(setOf("전체"), setOf("전체"), setOf("전체"))
        }
    }

    private fun setupViewPager() {
        pagerAdapter = TierPagerAdapter(this)
        binding.tierViewPager.adapter = pagerAdapter
        binding.tierViewPager.isUserInputEnabled = false // ViewPager2 스크롤 비활성화
    }

    private fun setupTabLayout() {
        with(binding.tierTabLayout) {
            addTab(newTab().setText("티어표"))
            addTab(newTab().setText("지도"))

            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    binding.tierViewPager.currentItem = tab.position
                    updateTabCategoryMargin(tab.position)
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {}
                override fun onTabReselected(tab: TabLayout.Tab) {}
            })
        }

        binding.tierViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.tierTabLayout.getTabAt(position)?.select()
                updateTabCategoryMargin(position)
            }
        })
    }

    private fun updateTabCategoryMargin(position: Int) {
        val marginEnd = if (position == 1) 0 else requireContext().dpToPx(60)
        binding.tierSvSelectedCategory.updateMarginEnd(marginEnd)
    }

    private fun setupButtons() {
        binding.tierIvCategoryBtn.setOnClickListener {
            val intent = Intent(requireContext(), TierCategoryActivity::class.java).apply {
                putStringArrayListExtra(
                    "selectedMenus",
                    ArrayList(viewModel.selectedMenus.value ?: emptySet())
                )
                putStringArrayListExtra(
                    "selectedSituations",
                    ArrayList(viewModel.selectedSituations.value ?: emptySet())
                )
                putStringArrayListExtra(
                    "selectedLocations",
                    ArrayList(viewModel.selectedLocations.value ?: emptySet())
                )
                putExtra("fromTabIndex", binding.tierTabLayout.selectedTabPosition)
            }
            tierCategoryActivityLauncher.launch(intent)
        }
        binding.tierFlIvSearch.setOnClickListener {
            startActivity(Intent(requireContext(), SearchActivity::class.java))
        }
        binding.btnBack.setOnClickListener {
            (requireActivity() as? MainActivity)?.let { mainActivity ->
                mainActivity.supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, HomeFragment())
                    .commit()
                mainActivity.binding.mainNavigation.selectedItemId = R.id.menu_home
            }
        }
    }

    private fun observeViewModel() {
        viewModel.selectedCategories.observe(viewLifecycleOwner) { categories ->
            updateCategoryLinearLayout(categories)
        }
    }

    private fun updateCategoryLinearLayout(categories: Set<String>) {
        binding.selectedCategoryLinearLayout.removeAllViews()
        categories.forEach { category ->
            binding.selectedCategoryLinearLayout.addView(createCategoryTextView(category))
        }
    }

    private fun createCategoryTextView(category: String): TextView {
        return TextView(requireContext()).apply {
            text = category
            setTextColor(ContextCompat.getColor(context, R.color.signature_1))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            background = ContextCompat.getDrawable(context, R.drawable.btn_tier_catetory_selected)
            val paddingHorizontal = getPadding(13f)
            val paddingVertical = getPadding(5.9f)
            setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical)
            layoutParams = ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply { marginEnd = getPadding(7f) }
        }
    }

    private fun handleCategoryActivityResult(data: Intent) {
        val selectedMenus = data.getStringArrayListExtra("selectedMenus")?.toSet() ?: emptySet()
        val selectedSituations = data.getStringArrayListExtra("selectedSituations")?.toSet() ?: emptySet()
        val selectedLocations = data.getStringArrayListExtra("selectedLocations")?.toSet() ?: emptySet()
        val fromTabIndex = TierScreenType.fromTabIdx(data.getIntExtra("fromTabIndex", 0))

        viewModel.setCategory(selectedMenus, selectedSituations, selectedLocations)

        if (getAccessToken(requireContext()) == null) {
            viewModel.loadRestaurant(fromTabIndex, false)
        } else {
            viewModel.loadRestaurant(fromTabIndex, true)
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

    private fun showMainContent() {
        binding.tierTvCategoryText.visibility = View.GONE
        binding.tierViewPager.visibility = View.VISIBLE
        binding.tierTabLayout.visibility = View.VISIBLE
        binding.tierClMiddleBar.visibility = View.VISIBLE
        binding.tierViewPager.post {
            pagerAdapter.refreshAllFragments()
        }
    }

    private fun Context.dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }

    private fun View.updateMarginEnd(marginEnd: Int) {
        val params = layoutParams as ViewGroup.MarginLayoutParams
        params.marginEnd = marginEnd
        layoutParams = params
    }

    private fun getPadding(dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics
        ).toInt()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
