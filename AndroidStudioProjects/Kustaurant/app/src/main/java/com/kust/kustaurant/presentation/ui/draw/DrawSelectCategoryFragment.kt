package com.kust.kustaurant.presentation.ui.draw

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.ToggleButton
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
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
    private lateinit var allMenuViews: List<LinearLayout>
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
            val selectedSituation = getSelectedTogglesText(binding.tierToggleSituationGroup)
            val selectedLocation = getSelectedTogglesText(binding.tierToggleLocationGroup)

            viewModel.applyFilters(selectedMenus, selectedSituation, selectedLocation)

            parentFragmentManager.beginTransaction()
                .replace(R.id.draw_fragment_container, DrawSelectResultFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setupToggleGroups() {
        setupToggleGroup(binding.tierToggleLocationGroup)
        setupToggleGroup(binding.tierToggleSituationGroup)
        initMenuClickListeners()
    }

    private fun observeViewModel() {
        viewModel.selectedMenus.observe(viewLifecycleOwner) { selectedMenus ->
            updateImageViewSelections(selectedMenus)
        }
        viewModel.selectedLocations.observe(viewLifecycleOwner) { selectedTypes ->
            updateToggleGroupSelection(binding.tierToggleLocationGroup, selectedTypes)
        }
        viewModel.selectedSituations.observe(viewLifecycleOwner) { selectedTypes ->
            updateToggleGroupSelection(binding.tierToggleSituationGroup, selectedTypes)
        }
    }

    private fun setupToggleGroup(toggleGroup: ViewGroup) {
        for (i in 0 until toggleGroup.childCount) {
            val toggleButton = toggleGroup.getChildAt(i) as ToggleButton
            toggleButton.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked)
                    toggleButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.signature_1))
                else
                    toggleButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.cement_4))

                if (isChecked && toggleButton.text.toString() == "전체") {
                    clearOtherToggles(toggleGroup, toggleButton)
                } else if (isChecked && hasOtherTogglesSelected(toggleGroup)) {
                    clearTotalToggle(toggleGroup)
                }
            }
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
        allMenuViews.forEach { linearLayout ->
            linearLayout.setOnClickListener {
                val textView = linearLayout.getChildAt(1) as? TextView
                textView?.let { tv ->
                    val menuText = tv.text.toString()
                    setupToggleMenuSelection(menuText, linearLayout)
                }
            }
        }
    }

    private fun setupToggleMenuSelection(menuText: String, view: LinearLayout) {
        val isSelected = !view.isSelected
        view.isSelected = isSelected
        applyMenuToggleUI(view, isSelected)

        if (menuText == "전체" && isSelected) {
            selectedMenus.clear()
            selectedMenus.add("전체")
            deselectOtherMenusToggle(view)
        } else {
            if (isSelected) {
                if (selectedMenus.contains("전체")) {
                    selectedMenus.remove("전체")
                    deselectTotalMenuToggle()
                }
                selectedMenus.add(menuText)
            } else {
                selectedMenus.remove(menuText)
            }
        }
    }

    private fun deselectOtherMenusToggle(excludeView: LinearLayout) {
        allMenuViews.filter { it != excludeView }.forEach { menuView ->
            menuView.isSelected = false
            applyMenuToggleUI(menuView, false)
        }
    }

    private fun deselectTotalMenuToggle() {
        val totalMenuView = binding.homeBtnAll
        totalMenuView.isSelected = false
        applyMenuToggleUI(totalMenuView, false)
    }

    private fun applyMenuToggleUI(view: View, isSelected: Boolean) {
        if (isSelected) {
            val drawable = GradientDrawable()
            drawable.shape = GradientDrawable.RECTANGLE
            drawable.setStroke(1, ContextCompat.getColor(requireContext(), R.color.signature_1))
            drawable.cornerRadius = 15f
            view.setPadding(1)
            view.background = drawable
        } else {
            view.setBackgroundResource(R.drawable.all_radius_15_none)
        }
    }
}