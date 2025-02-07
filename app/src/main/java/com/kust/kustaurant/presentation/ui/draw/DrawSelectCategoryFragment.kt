package com.kust.kustaurant.presentation.ui.draw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.kust.kustaurant.R
import com.kust.kustaurant.databinding.FragmentDrawSelectCategoryBinding
import com.kust.kustaurant.presentation.common.CategoryData
import com.kust.kustaurant.presentation.ui.home.HomeFragment.CategorySpaceDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DrawSelectCategoryFragment : Fragment(), DrawCategoryAdapter.CategoryItemClickListener {

    private var _binding: FragmentDrawSelectCategoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DrawViewModel by activityViewModels()
    private lateinit var categoryAdapter: DrawCategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDrawSelectCategoryBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCategoryRV()
        setupToggleGroups()
        observeViewModel()

        binding.drawBtnDrawResult.setOnClickListener {
            val selectedLocation = getSelectedTogglesText(binding.tierToggleLocationGroup)
            viewModel.updateSelectedLocations(selectedLocation)

            parentFragmentManager.beginTransaction()
                .replace(R.id.draw_fragment_container, DrawResultFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setupToggleGroups() {
        setupToggleGroup(binding.tierToggleLocationGroup)
    }

    private fun observeViewModel() {
        viewModel.selectedLocations.observe(viewLifecycleOwner) { selectedTypes ->
            updateToggleGroupSelection(binding.tierToggleLocationGroup, selectedTypes)
        }

        viewModel.selectedMenus.observe(viewLifecycleOwner) { selectedMenus ->
            categoryAdapter.updateSelectedItems(selectedMenus)
        }
    }

    private fun setupToggleGroup(toggleGroup: ViewGroup) {
        for (i in 0 until toggleGroup.childCount) {
            val toggleButton = toggleGroup.getChildAt(i) as ToggleButton
            toggleButton.setOnCheckedChangeListener { _, isChecked ->
                if (!isChecked && !hasOtherTogglesSelected(toggleGroup)) {
                    // 마지막 버튼 해제 방지
                    toggleButton.isChecked = true
                } else {
                    if (isChecked) {
                        toggleButton.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.signature_1
                            )
                        )
                        if (toggleButton.text.toString() == "전체" || toggleButton.text.toString() == "제휴업체") {
                            clearOtherToggles(toggleGroup, toggleButton)
                        }
                    } else {
                        toggleButton.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.cement_4
                            )
                        )
                    }
                    // "전체" 버튼이 선택되지 않도록 처리
                    if (isChecked && (toggleButton.text.toString() != "전체" && toggleButton.text.toString() != "제휴업체")) {
                        clearTotalToggle(toggleGroup)
                    }
                }
            }
        }
    }

    private fun clearOtherToggles(toggleGroup: ViewGroup, selectedToggle: ToggleButton) {
        toggleGroup.post {
            for (i in 0 until toggleGroup.childCount) {
                val toggleButton = toggleGroup.getChildAt(i) as ToggleButton
                if (toggleButton != selectedToggle) {
                    toggleButton.isChecked = false
                }
            }
        }
    }

    private fun clearTotalToggle(toggleGroup: ViewGroup) {
        toggleGroup.post {
            for (i in 0 until toggleGroup.childCount) {
                val toggleButton = toggleGroup.getChildAt(i) as ToggleButton
                if (toggleButton.text.toString() == "전체") {
                    toggleButton.isChecked = false
                }
            }
        }
    }

    private fun hasOtherTogglesSelected(toggleGroup: ViewGroup): Boolean {
        for (i in 0 until toggleGroup.childCount) {
            val toggleButton = toggleGroup.getChildAt(i) as ToggleButton
            if (toggleButton.isChecked) {
                return true
            }
        }
        return false
    }

    private fun getSelectedTogglesText(toggleGroup: ViewGroup): Set<String> {
        val selectedText = mutableSetOf<String>()
        for (i in 0 until toggleGroup.childCount) {
            val toggleButton = toggleGroup.getChildAt(i) as ToggleButton
            if (toggleButton.isChecked) {
                selectedText.add(toggleButton.text.toString())
            }
        }
        return selectedText
    }

    private fun updateToggleGroupSelection(toggleGroup: ViewGroup, selectedItems: Set<String>) {
        for (i in 0 until toggleGroup.childCount) {
            val toggleButton = toggleGroup.getChildAt(i) as ToggleButton
            toggleButton.isChecked = selectedItems.contains(toggleButton.text.toString())
        }
    }

    private fun setupCategoryRV() {
        val categoryList = CategoryData.categoryList
        categoryAdapter = DrawCategoryAdapter(categoryList, this)
        binding.categoryRecyclerView.apply {
            val spanCount = 4
            val spacing = 10
            // spacing을 dp에서 px로 변환
            val spacingInPx = (spacing * resources.displayMetrics.density).toInt()
            addItemDecoration(CategorySpaceDecoration(spanCount, spacingInPx))
            layoutManager = GridLayoutManager(requireContext(), spanCount)
            adapter = categoryAdapter

            (itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
        }
    }

    override fun onCategoryItemClick(category: String) {
        val currentSelectedMenus = viewModel.selectedMenus.value?.toMutableSet() ?: mutableSetOf()

        if (category == "전체" || category == "제휴업체") {
            currentSelectedMenus.clear()
            currentSelectedMenus.add(category)
        } else {
            currentSelectedMenus.remove("전체")
            currentSelectedMenus.remove("제휴업체")
            if (currentSelectedMenus.contains(category)) {
                currentSelectedMenus.remove(category)
            } else {
                currentSelectedMenus.add(category)
            }
        }

        viewModel.updateSelectedMenus(currentSelectedMenus)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}