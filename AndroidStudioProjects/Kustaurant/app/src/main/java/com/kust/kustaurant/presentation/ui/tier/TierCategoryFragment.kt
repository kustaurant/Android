package com.kust.kustaurant.presentation.ui.tier

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.kust.kustaurant.R
import com.kust.kustaurant.databinding.FragmentTierCategorySelectBinding
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
        binding.tierBtnApply.setOnClickListener {
            applyFilters()
        }

        fromTabIndex = arguments?.getInt("fromTabIndex") ?: 0

        // 현재 선택된 필터 값들을 적용
        updateToggleGroupSelection(binding.tierToggleTypeGroup, viewModel.selectedMenus.value ?: setOf())
        updateToggleGroupSelection(binding.tierToggleSituationGroup, viewModel.selectedSituations.value ?: setOf())
        updateToggleGroupSelection(binding.tierToggleLocationGroup, viewModel.selectedLocations.value ?: setOf())

        // 초기 상태에서 적용하기 버튼 상태 업데이트
        updateApplyButtonState()

        // location_info 텍스트뷰 클릭 리스너 설정
        binding.tierLocationInfo.setOnClickListener {
            navigateToMapFragment()
        }
    }

    private fun setupToggleGroups() {
        setupToggleGroup(binding.tierToggleTypeGroup)
        setupToggleGroup(binding.tierToggleSituationGroup)
        setupToggleGroup(binding.tierToggleLocationGroup)
    }

    private fun setupToggleGroup(toggleGroup: ViewGroup) {
        for (i in 0 until toggleGroup.childCount) {
            val toggleButton = toggleGroup.getChildAt(i) as ToggleButton
            toggleButton.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked)
                    toggleButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.signature_1))
                else
                    toggleButton.setTextColor(ContextCompat.getColor(requireContext(),R.color.cement_4))

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
        viewModel.selectedMenus.observe(viewLifecycleOwner) { selectedMenus ->
            updateToggleGroupSelection(binding.tierToggleTypeGroup, selectedMenus)
        }

        viewModel.selectedSituations.observe(viewLifecycleOwner) { selectedSituations ->
            updateToggleGroupSelection(binding.tierToggleSituationGroup, selectedSituations)
        }

        viewModel.selectedLocations.observe(viewLifecycleOwner) { selectedLocations ->
            updateToggleGroupSelection(binding.tierToggleLocationGroup, selectedLocations)
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
        val selectedType = getSelectedTogglesText(binding.tierToggleTypeGroup)
        val selectedSituation = getSelectedTogglesText(binding.tierToggleSituationGroup)
        val selectedLocation = getSelectedTogglesText(binding.tierToggleLocationGroup)

        val hasChanges = viewModel.hasFilterChanged(selectedType, selectedSituation, selectedLocation)
        val isAnyGroupSelected = selectedType.isNotEmpty() && selectedSituation.isNotEmpty() && selectedLocation.isNotEmpty()

        binding.tierBtnApply.isEnabled = hasChanges && isAnyGroupSelected
        if (binding.tierBtnApply.isEnabled) {
            binding.tierBtnApply.setBackgroundResource(R.drawable.btn_tier_category_apply_active)
            binding.tierBtnApply.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        } else {
            binding.tierBtnApply.setBackgroundResource(R.drawable.btn_tier_category_apply_inactive)
            binding.tierBtnApply.setTextColor(ContextCompat.getColor(requireContext(), R.color.cement_4))
        }
    }


    private fun applyFilters() {
        val selectedType = getSelectedTogglesText(binding.tierToggleTypeGroup)
        val selectedSituation = getSelectedTogglesText(binding.tierToggleSituationGroup)
        val selectedLocation = getSelectedTogglesText(binding.tierToggleLocationGroup)

        viewModel.applyFilters(selectedType, selectedSituation, selectedLocation, fromTabIndex)

        // 이 Fragment 제거
        parentFragmentManager.beginTransaction().remove(this).commit()
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

    private fun navigateToMapFragment() {
        val tierFragment = parentFragment
        if (tierFragment != null) {
            Log.e("TierCategoryFragment", "parentFragment class: ${tierFragment::class.java.simpleName}")
            if (tierFragment is TierFragment) {
                tierFragment.navigateToTab(1)
            } else {
                Log.e("TierCategoryFragment", "parentFragment is NOT an instance of TierFragment")
            }
        } else {
            Log.e("TierCategoryFragment", "parentFragment is null")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

