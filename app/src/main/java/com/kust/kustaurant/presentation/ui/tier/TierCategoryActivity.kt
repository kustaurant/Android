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
import androidx.core.content.ContextCompat
import com.kust.kustaurant.R
import com.kust.kustaurant.databinding.ActivityTierCategorySelectBinding
import com.kust.kustaurant.presentation.common.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TierCategoryActivity : BaseActivity() {
    private lateinit var binding: ActivityTierCategorySelectBinding
    private var fromTabIndex: Int = 0

    private val initialFilters: MutableMap<String, Set<String>> = mutableMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTierCategorySelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeInitialFilters()
        setupToggleGroups()
        updateInitialSelection()
        setupClickListeners()
    }

    private fun initializeInitialFilters() {
        fromTabIndex = intent.getIntExtra("fromTabIndex", 0)
        initialFilters["menus"] = intent.getStringArrayListExtra("selectedMenus")?.toSet() ?: emptySet()
        initialFilters["situations"] = intent.getStringArrayListExtra("selectedSituations")?.toSet() ?: emptySet()
        initialFilters["locations"] = intent.getStringArrayListExtra("selectedLocations")?.toSet() ?: emptySet()
    }

    private fun setupToggleGroups() {
        listOf(
            binding.tierToggleTypeGroup,
            binding.tierToggleSituationGroup,
            binding.tierToggleLocationGroup
        ).forEach { toggleGroup ->
            setupToggleGroup(toggleGroup)
        }
    }

    private fun setupToggleGroup(toggleGroup: ViewGroup) {
        for (i in 0 until toggleGroup.childCount) {
            val toggleButton = toggleGroup.getChildAt(i) as ToggleButton
            toggleButton.setOnCheckedChangeListener { _, isChecked ->
                updateToggleButtonStyle(toggleButton, isChecked)
                handleToggleGroupState(toggleGroup, toggleButton, isChecked)
                updateApplyButtonState()
            }
        }
    }

    private fun updateToggleButtonStyle(toggleButton: ToggleButton, isChecked: Boolean) {
        toggleButton.setTextColor(
            ContextCompat.getColor(
                this,
                if (isChecked) R.color.signature_1 else R.color.cement_4
            )
        )
    }

    private fun handleToggleGroupState(
        toggleGroup: ViewGroup,
        selectedToggle: ToggleButton,
        isChecked: Boolean
    ) {
        when {
            isChecked && selectedToggle.text in listOf("전체", "제휴업체") -> clearOtherToggles(toggleGroup, selectedToggle)
            isChecked && hasOtherTogglesSelected(toggleGroup) -> clearTotalToggle(toggleGroup)
        }
    }

    private fun updateInitialSelection() {
        mapOf(
            binding.tierToggleTypeGroup to initialFilters["menus"],
            binding.tierToggleSituationGroup to initialFilters["situations"],
            binding.tierToggleLocationGroup to initialFilters["locations"]
        ).forEach { (group, selection) ->
            updateToggleGroupSelection(group, selection.orEmpty())
        }
    }

    private fun updateToggleGroupSelection(toggleGroup: ViewGroup, selectedItems: Set<String>) {
        for (i in 0 until toggleGroup.childCount) {
            val toggleButton = toggleGroup.getChildAt(i) as ToggleButton
            toggleButton.isChecked = selectedItems.contains(toggleButton.text.toString())
        }
    }

    private fun getSelectedToggles(toggleGroup: ViewGroup): Set<String> {
        return (0 until toggleGroup.childCount)
            .map { toggleGroup.getChildAt(it) as ToggleButton }
            .filter { it.isChecked }
            .map { it.text.toString() }
            .toSet()
    }

    private fun updateApplyButtonState() {
        val selectedFilters = mapOf(
            "menus" to getSelectedToggles(binding.tierToggleTypeGroup),
            "situations" to getSelectedToggles(binding.tierToggleSituationGroup),
            "locations" to getSelectedToggles(binding.tierToggleLocationGroup)
        )

        val hasChanges = selectedFilters.any { (key, value) ->
            value != initialFilters[key]
        }

        val isAllGroupsSelected = selectedFilters.all { it.value.isNotEmpty() }

        binding.tierBtnApply.isEnabled = hasChanges && isAllGroupsSelected
        binding.tierBtnApply.apply {
            setBackgroundResource(
                if (isEnabled) R.drawable.btn_tier_category_apply_active
                else R.drawable.btn_tier_category_apply_inactive
            )
            setTextColor(
                ContextCompat.getColor(
                    this@TierCategoryActivity,
                    if (isEnabled) R.color.white else R.color.cement_4
                )
            )
        }
    }

    private fun clearOtherToggles(toggleGroup: ViewGroup, selectedToggle: ToggleButton) {
        for (i in 0 until toggleGroup.childCount) {
            val toggleButton = toggleGroup.getChildAt(i) as ToggleButton
            if (toggleButton != selectedToggle) toggleButton.isChecked = false
        }
    }

    private fun clearTotalToggle(toggleGroup: ViewGroup) {
        for (i in 0 until toggleGroup.childCount) {
            val toggleButton = toggleGroup.getChildAt(i) as ToggleButton
            if (toggleButton.text in listOf("전체", "제휴업체")) toggleButton.isChecked = false
        }
    }

    private fun hasOtherTogglesSelected(toggleGroup: ViewGroup): Boolean {
        return (0 until toggleGroup.childCount)
            .map { toggleGroup.getChildAt(it) as ToggleButton }
            .any { it.isChecked && it.text !in listOf("전체", "제휴업체") }
    }

    private fun setupClickListeners() {
        binding.tierBtnApply.setOnClickListener { returnSelectedFilters() }
        binding.btnBack.setOnClickListener { finish() }
        binding.tierInfo.setOnClickListener { showPopup() }
    }

    private fun returnSelectedFilters() {
        val resultIntent = Intent().apply {
            putStringArrayListExtra("selectedMenus", ArrayList(getSelectedToggles(binding.tierToggleTypeGroup)))
            putStringArrayListExtra("selectedSituations", ArrayList(getSelectedToggles(binding.tierToggleSituationGroup)))
            putStringArrayListExtra("selectedLocations", ArrayList(getSelectedToggles(binding.tierToggleLocationGroup)))
            putExtra("fromTabIndex", fromTabIndex)
        }
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    private fun showPopup() {
        val dialog = Dialog(this).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.popup_tier)
            window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                setGravity(Gravity.CENTER)
            }
            setCanceledOnTouchOutside(true)
        }

        dialog.findViewById<TextView>(R.id.tier_popup_description).text = Html.fromHtml(
            """
            식당의 티어는 여러분이 부여한 소중한 <font color="#43AB38">평가 점수</font>가 반영되어 결정됩니다!<br><br>
            평가는 각 식당의 상세 페이지에서 할 수 있으며, 점수는 <font color="#43AB38">5점 만점</font>입니다.<br><br>
            식당에 대한 <font color="#43AB38">평가의 평균 점수</font>로 티어가 산출됩니다.
            """.trimIndent(), Html.FROM_HTML_MODE_COMPACT
        )

        dialog.show()
    }
}
