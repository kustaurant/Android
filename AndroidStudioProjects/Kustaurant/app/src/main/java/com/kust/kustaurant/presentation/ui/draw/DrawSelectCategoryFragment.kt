package com.kust.kustaurant.presentation.ui.draw

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.ToggleButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.kust.kustaurant.R
import com.kust.kustaurant.databinding.FragmentDrawSelectCategoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DrawSelectCategoryFragment : Fragment() {
    private var _binding : FragmentDrawSelectCategoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel : DrawViewModel by activityViewModels()
    private var selectedMenus = mutableSetOf<String>("전체")
    private lateinit var allMenuViews: List<ConstraintLayout>
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
        allMenuViews= listOf(
            binding.homeBtnAll,
            binding.homeBtnKorea,
            binding.homeBtnJapan,
            binding.homeBtnChina,
            binding.homeBtnWestern,
            binding.homeBtnAsian,
            binding.homeBtnMeat,
            binding.homeBtnSeafood,
            binding.homeBtnChicken,
            binding.homeBtnHamburgerPizza,
            binding.homeBtnTteokbokki,
            binding.homeBtnBeer,
            binding.homeBtnCafe,
            binding.homeBtnBakery,
            binding.homeBtnSalad,
            binding.homeBtnBenefit
        )

        setupToggleGroups()
        observeViewModel()

        binding.drawBtnDrawRandomRestaurants.setOnClickListener {
            //val selectedSituation = getSelectedTogglesText(binding.tierToggleSituationGroup)
            val selectedLocation = getSelectedTogglesText(binding.tierToggleLocationGroup)

            viewModel.applyFilters(selectedMenus,selectedLocation)

            parentFragmentManager.beginTransaction()
                .replace(R.id.draw_fragment_container, DrawSelectResultFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setupToggleGroups() {
        setupToggleGroup(binding.tierToggleLocationGroup)
        //setupToggleGroup(binding.tierToggleSituationGroup)
        initMenuClickListeners()
    }

    private fun observeViewModel() {
        viewModel.selectedMenus.observe(viewLifecycleOwner) { selectedMenus ->
            updateImageViewSelections(selectedMenus)
        }
        viewModel.selectedLocations.observe(viewLifecycleOwner) { selectedTypes ->
            updateToggleGroupSelection(binding.tierToggleLocationGroup, selectedTypes)
        }
    }

    private fun setupToggleGroup(toggleGroup: ViewGroup) {
        for (i in 0 until toggleGroup.childCount) {
            val toggleButton = toggleGroup.getChildAt(i) as ToggleButton
            toggleButton.setOnCheckedChangeListener { _, isChecked ->
                if (!isChecked && !hasOtherTogglesSelected(toggleGroup)) {
                    // Prevent unchecking the last selected button
                    toggleButton.isChecked = true
                } else {
                    if (isChecked) {
                        // Change the text color to active
                        toggleButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.signature_1))

                        if (toggleButton.text.toString() == "전체" || toggleButton.text.toString() == "제휴업체") {
                            clearOtherToggles(toggleGroup, toggleButton)
                        }
                    } else {
                        // Change the text color to inactive
                        toggleButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.cement_4))
                    }

                    // Ensure "전체" is deselected if any other button is selected
                    if (isChecked && (toggleButton.text.toString() != "전체" || toggleButton.text.toString() != "제휴업체")) {
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

    // 다른 버튼이 선택된 경우 "전체" 버튼을 해제
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

    // 하나 이상의 토글 버튼이 선택되어 있는지 확인
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

    private fun updateImageViewSelections(selectedMenus: Set<String>) {
        allMenuViews.forEach { linearLayout ->
            val textView = linearLayout.getChildAt(1) as? TextView
            if (textView != null) {
                val isSelected = selectedMenus.contains(textView.text.toString())
                linearLayout.isSelected = isSelected
                applyMenuToggleUI(linearLayout, isSelected)
            }
        }
    }

    private fun initMenuClickListeners() {
        allMenuViews.forEach { constraintLayout ->
            constraintLayout.setOnClickListener {
                val textView = constraintLayout.getChildAt(1) as? TextView
                textView?.let { tv ->
                    val menuText = tv.text.toString()
                    setupToggleMenuSelection(menuText, constraintLayout)
                }
            }
        }
    }
    private fun setupToggleMenuSelection(menuText: String, view: ConstraintLayout) {
        val isSelected = !view.isSelected
        view.isSelected = isSelected
        applyMenuToggleUI(view, isSelected)

        if ((menuText == "전체" || menuText == "제휴업체") && isSelected) {
            // "전체" 또는 "제휴"가 선택되었을 때 다른 모든 메뉴 선택 해제
            selectedMenus.clear()
            selectedMenus.add(menuText)
            deselectOtherMenusToggle(view)
        } else {
            if (isSelected) {
                // "전체" 또는 "제휴"가 이미 선택되었으면 해제하고 다른 메뉴 선택
                if (selectedMenus.contains("전체") || selectedMenus.contains("제휴업체")) {
                    selectedMenus.remove("전체")
                    selectedMenus.remove("제휴업체")
                    deselectSpecialMenuToggles() // "전체"와 "제휴" 해제
                }
                selectedMenus.add(menuText)
            } else {
                selectedMenus.remove(menuText)
            }
        }
    }

    // "전체" 또는 "제휴업체"가 선택되었을 때 다른 메뉴 해제
    private fun deselectOtherMenusToggle(excludeView: ConstraintLayout) {
        allMenuViews.filter { it != excludeView }.forEach { menuView ->
            menuView.isSelected = false
            applyMenuToggleUI(menuView, false)
        }
    }

    // "전체" 또는 "제휴업체" 버튼 해제
    private fun deselectSpecialMenuToggles() {
        val totalMenuView = binding.homeBtnAll
        val benefitMenuView = binding.homeBtnBenefit

        totalMenuView.isSelected = false
        benefitMenuView.isSelected = false

        applyMenuToggleUI(totalMenuView, false)
        applyMenuToggleUI(benefitMenuView, false)
    }

    private fun applyMenuToggleUI(view: View, isSelected: Boolean) {
        if (isSelected) {
            val drawable = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE

                // Convert dp to px for stroke width and corner radius
                val strokeWidth = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    1.2f,
                    view.context.resources.displayMetrics
                ).toInt()

                val cornerRadius = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    15f,
                    view.context.resources.displayMetrics
                )

                setStroke(strokeWidth, ContextCompat.getColor(view.context, R.color.signature_1))
                this.cornerRadius = cornerRadius
            }

            view.background = drawable
        } else {
            view.setBackgroundResource(R.drawable.all_radius_15_none)
        }
    }
}
