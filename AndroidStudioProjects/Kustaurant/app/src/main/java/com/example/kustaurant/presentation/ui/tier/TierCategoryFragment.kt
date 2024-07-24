package com.example.kustaurant.presentation.ui.tier

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.kustaurant.R
import com.example.kustaurant.databinding.FragmentTierCategorySelectBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TierCategoryFragment : Fragment() {

    private var _binding: FragmentTierCategorySelectBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TierViewModel by activityViewModels()
    private var fromTabIndex: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTierCategorySelectBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToggleGroups()
        observeViewModel()
        binding.applyButton.setOnClickListener {
            applyFilters()
        }

        fromTabIndex = arguments?.getInt("fromTabIndex") ?: 0

        // 현재 선택된 필터 값들을 적용
        updateToggleGroupSelection(binding.typeToggleGroup, viewModel.selectedTypes.value ?: setOf())
        updateToggleGroupSelection(binding.situationToggleGroup, viewModel.selectedSituations.value ?: setOf())
        updateToggleGroupSelection(binding.locationToggleGroup, viewModel.selectedLocations.value ?: setOf())

        // 초기 상태에서 적용하기 버튼 상태 업데이트
        updateApplyButtonState()
    }
    private fun setupToggleGroups() {
        setupToggleGroup(binding.typeToggleGroup)
        setupToggleGroup(binding.situationToggleGroup)
        setupToggleGroup(binding.locationToggleGroup)
    }

    private fun setupToggleGroup(toggleGroup: ViewGroup) {
        for (i in 0 until toggleGroup.childCount) {
            val toggleButton = toggleGroup.getChildAt(i) as ToggleButton
            toggleButton.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked && toggleButton.text.toString() == "전체") {
                    clearOtherToggles(toggleGroup, toggleButton)
                } else if (isChecked && hasOtherTogglesSelected(toggleGroup)) {
                    clearTotalToggle(toggleGroup)
                }
                updateApplyButtonState()
            }
        }
    }

    private fun observeViewModel() {
        viewModel.selectedTypes.observe(viewLifecycleOwner) { selectedTypes ->
            updateToggleGroupSelection(binding.typeToggleGroup, selectedTypes)
        }

        viewModel.selectedSituations.observe(viewLifecycleOwner) { selectedSituations ->
            updateToggleGroupSelection(binding.situationToggleGroup, selectedSituations)
        }

        viewModel.selectedLocations.observe(viewLifecycleOwner) { selectedLocations ->
            updateToggleGroupSelection(binding.locationToggleGroup, selectedLocations)
        }
    }

    private fun updateToggleGroupSelection(toggleGroup: ViewGroup, selectedItems: Set<String>) {
        for (i in 0 until toggleGroup.childCount) {
            val toggleButton = toggleGroup.getChildAt(i) as ToggleButton
            toggleButton.isChecked = selectedItems.contains(toggleButton.text.toString())
        }
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

    private fun updateApplyButtonState() {
        val selectedType = getSelectedTogglesText(binding.typeToggleGroup)
        val selectedSituation = getSelectedTogglesText(binding.situationToggleGroup)
        val selectedLocation = getSelectedTogglesText(binding.locationToggleGroup)

        val hasChanges = viewModel.hasFilterChanged(selectedType, selectedSituation, selectedLocation)
        val isAnyGroupSelected = selectedType.isNotEmpty() && selectedSituation.isNotEmpty() && selectedLocation.isNotEmpty()

        binding.applyButton.isEnabled = hasChanges && isAnyGroupSelected
        if (binding.applyButton.isEnabled) {
            binding.applyButton.setBackgroundResource(R.drawable.btn_tier_category_apply_active)
        } else {
            binding.applyButton.setBackgroundResource(R.drawable.btn_tier_category_apply_inactive)
        }
    }

    private fun applyFilters() {
        val selectedType = getSelectedTogglesText(binding.typeToggleGroup)
        val selectedSituation = getSelectedTogglesText(binding.situationToggleGroup)
        val selectedLocation = getSelectedTogglesText(binding.locationToggleGroup)


        viewModel.applyFilters(selectedType, selectedSituation, selectedLocation)

        viewModel.loadTierList(
            selectedType.joinToString(", "),
            selectedSituation.joinToString(", "),
            selectedLocation.joinToString(", ")
        )

        // Navigate back to the respective tab
        (requireParentFragment() as? TierFragment)?.let {
            it.binding.viewPager.currentItem = fromTabIndex
        }
    }

    private fun clearOtherToggles(toggleGroup: ViewGroup, selectedToggle: ToggleButton) {
        for (i in 0 until toggleGroup.childCount) {
            val toggleButton = toggleGroup.getChildAt(i) as ToggleButton
            if (toggleButton != selectedToggle) {
                toggleButton.isChecked = false
            }
        }
    }

    private fun clearTotalToggle(toggleGroup: ViewGroup) {
        for (i in 0 until toggleGroup.childCount) {
            val toggleButton = toggleGroup.getChildAt(i) as ToggleButton
            if (toggleButton.text.toString() == "전체") {
                toggleButton.isChecked = false
            }
        }
    }

    private fun hasOtherTogglesSelected(toggleGroup: ViewGroup): Boolean {
        for (i in 0 until toggleGroup.childCount) {
            val toggleButton = toggleGroup.getChildAt(i) as ToggleButton
            if (toggleButton.isChecked && toggleButton.text.toString() != "전체") {
                return true
            }
        }
        return false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
