package com.kust.kustaurant.presentation.ui.tier

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.kust.kustaurant.R
import com.kust.kustaurant.databinding.ActivityTierCategorySelectBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TierCategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTierCategorySelectBinding
    private var fromTabIndex: Int = 0

    private var initialSelectedMenus: Set<String> = emptySet()
    private var initialSelectedSituations: Set<String> = emptySet()
    private var initialSelectedLocations: Set<String> = emptySet()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTierCategorySelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fromTabIndex = intent.getIntExtra("fromTabIndex", 0)
        initialSelectedMenus = intent.getStringArrayListExtra("selectedMenus")?.toSet() ?: emptySet()
        initialSelectedSituations = intent.getStringArrayListExtra("selectedSituations")?.toSet() ?: emptySet()
        initialSelectedLocations = intent.getStringArrayListExtra("selectedLocations")?.toSet() ?: emptySet()

        setupToggleGroups()
        updateInitialSelection()
        setupClickListeners()
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
                if (isChecked) {
                    toggleButton.setTextColor(ContextCompat.getColor(this, R.color.signature_1))
                } else {
                    toggleButton.setTextColor(ContextCompat.getColor(this, R.color.cement_4))
                }

                if ((isChecked && toggleButton.text.toString() == "전체") || (isChecked && toggleButton.text.toString() == "제휴업체")) {
                    clearOtherToggles(toggleGroup, toggleButton)
                } else if (isChecked && hasOtherTogglesSelected(toggleGroup)) {
                    clearTotalToggle(toggleGroup)
                }

                updateApplyButtonState()
            }
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

        val hasChanges = hasFilterChanged(selectedType, selectedSituation, selectedLocation)
        val isAnyGroupSelected = selectedType.isNotEmpty() && selectedSituation.isNotEmpty() && selectedLocation.isNotEmpty()
        val isDifferentFromInitial = selectedType != initialSelectedMenus ||
                selectedSituation != initialSelectedSituations ||
                selectedLocation != initialSelectedLocations

        binding.tierBtnApply.isEnabled = hasChanges && isAnyGroupSelected && isDifferentFromInitial

        if (binding.tierBtnApply.isEnabled) {
            binding.tierBtnApply.setBackgroundResource(R.drawable.btn_tier_category_apply_active)
            binding.tierBtnApply.setTextColor(ContextCompat.getColor(this, R.color.white))
        } else {
            binding.tierBtnApply.setBackgroundResource(R.drawable.btn_tier_category_apply_inactive)
            binding.tierBtnApply.setTextColor(ContextCompat.getColor(this, R.color.cement_4))
        }
    }

    private fun hasFilterChanged(
        currentTypes: Set<String>,
        currentSituations: Set<String>,
        currentLocations: Set<String>
    ): Boolean {
        return (currentTypes != initialSelectedMenus ||
                currentSituations != initialSelectedSituations ||
                currentLocations != initialSelectedLocations)
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
            if (toggleButton.text.toString() == "전체" || toggleButton.text.toString() == "제휴업체") {
                toggleButton.isChecked = false
            }
        }
    }

    private fun hasOtherTogglesSelected(toggleGroup: ViewGroup): Boolean {
        for (i in 0 until toggleGroup.childCount) {
            val toggleButton = toggleGroup.getChildAt(i) as ToggleButton
            if ((toggleButton.isChecked && toggleButton.text.toString() != "전체") || (toggleButton.isChecked && toggleButton.text.toString() != "제휴업체")) {
                return true
            }
        }
        return false
    }

    private fun showPopup() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.popup_tier)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val tierInfoDescriptionTextView = dialog.findViewById<TextView>(R.id.tier_popup_description)
        val htmlText = """
        식당의 티어는 여러분이 부여한 소중한 <font color="#43AB38">평가 점수</font>가 반영되어 결정됩니다!<br><br>
        평가는 각 식당의 상세 페이지에서 할 수 있으며, 점수는 <font color="#43AB38">5점 만점</font>입니다.<br><br>
        식당에 대한 <font color="#43AB38">평가의 평균 점수</font>로 티어가 산출됩니다.
        """.trimIndent()
        tierInfoDescriptionTextView.text = Html.fromHtml(htmlText, Html.FROM_HTML_MODE_COMPACT)

        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setGravity(Gravity.CENTER)
        dialog.show()
        dialog.setCanceledOnTouchOutside(true)
    }

    private fun setupClickListeners() {
        binding.tierBtnApply.setOnClickListener {
            val selectedType = getSelectedTogglesText(binding.tierToggleTypeGroup)
            val selectedSituation = getSelectedTogglesText(binding.tierToggleSituationGroup)
            val selectedLocation = getSelectedTogglesText(binding.tierToggleLocationGroup)

            val resultIntent = Intent().apply {
                putStringArrayListExtra("selectedMenus", ArrayList(selectedType))
                putStringArrayListExtra("selectedSituations", ArrayList(selectedSituation))
                putStringArrayListExtra("selectedLocations", ArrayList(selectedLocation))
                putExtra("fromTabIndex", fromTabIndex)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.tierInfo.setOnClickListener {
            showPopup()
        }
    }

    private fun updateInitialSelection() {
        updateToggleGroupSelection(binding.tierToggleTypeGroup, initialSelectedMenus)
        updateToggleGroupSelection(binding.tierToggleSituationGroup, initialSelectedSituations)
        updateToggleGroupSelection(binding.tierToggleLocationGroup, initialSelectedLocations)
    }
}
