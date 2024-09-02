package com.kust.kustaurant.presentation.ui.tier

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
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

        binding.tierInfo.setOnClickListener {
            showPopup()
        }

        binding.tierLocationInfo.setOnClickListener {
              val tierFragment = requireActivity().supportFragmentManager.fragments.find { it is TierFragment } as? TierFragment

            if (tierFragment != null) {
                tierFragment.showMainContent()
                Log.d("TierCategoryFragment", "TierFragment found and showMainContent called")

                // 현재 프래그먼트를 제거
                parentFragmentManager.beginTransaction()
                    .remove(this@TierCategoryFragment)
                    .commit()

                // ViewPager의 두 번째 탭으로 이동
                tierFragment.binding.tierViewPager.post {
                    tierFragment.binding.tierViewPager.currentItem = 1
                }
            } else {
                Log.e("TierCategoryFragment", "TierFragment not found")
            }
        }





        fromTabIndex = arguments?.getInt("fromTabIndex") ?: 0

        // 현재 선택된 필터 값들을 적용
        updateToggleGroupSelection(binding.tierToggleTypeGroup, viewModel.selectedMenus.value ?: setOf())
        updateToggleGroupSelection(binding.tierToggleSituationGroup, viewModel.selectedSituations.value ?: setOf())
        updateToggleGroupSelection(binding.tierToggleLocationGroup, viewModel.selectedLocations.value ?: setOf())

        // 초기 상태에서 적용하기 버튼 상태 업데이트
        updateApplyButtonState()

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

    private fun showPopup() {
        // 팝업창을 위한 Dialog 생성
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.popup_tier)

        // Dialog의 배경을 투명하게 설정
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 텍스트뷰 가져오기
        val tierInfoDescriptionTextView = dialog.findViewById<TextView>(R.id.tier_popup_description)

        // HTML 태그를 사용하여 텍스트의 일부 색상을 변경
        val htmlText = """
        식당의 티어는 여러분이 부여한 소중한 <font color="#43AB38">평가 점수</font>가 반영되어 결정됩니다!<br><br>
        평가는 각 식당의 상세 페이지에서 할 수 있으며, 점수는 <font color="#43AB38">5점 만점</font>입니다.<br><br>
        식당에 대한 여러 <font color="#43AB38">평가의 평균 점수</font>가 가장 높은 식당 순으로 티어가 산출되어 우선적으로 노출됩니다!
    """.trimIndent()

        tierInfoDescriptionTextView.text = Html.fromHtml(htmlText)

        // Dialog 크기와 위치 설정
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        dialog.window?.setGravity(Gravity.CENTER)

        // Dialog를 표시
        dialog.show()

        // Dialog 외부를 터치하면 닫히도록 설정
        dialog.setCanceledOnTouchOutside(true)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
