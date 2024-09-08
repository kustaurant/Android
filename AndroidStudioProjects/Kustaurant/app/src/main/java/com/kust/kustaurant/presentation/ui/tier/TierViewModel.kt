package com.kust.kustaurant.presentation.ui.tier

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kust.kustaurant.data.model.TierMapData
import com.kust.kustaurant.domain.model.TierRestaurant
import com.kust.kustaurant.domain.usecase.tier.GetTierRestaurantListUseCase
import com.kust.kustaurant.domain.usecase.tier.GetTierRestaurantMapUseCase
import com.kust.kustaurant.presentation.util.CategoryIdMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TierViewModel @Inject constructor(
    private val getTierRestaurantListUseCase: GetTierRestaurantListUseCase,
    private val getTierRestaurantMapUseCase: GetTierRestaurantMapUseCase
) : ViewModel() {
    private val _filterApplied = MutableLiveData<Boolean>()
    val filterApplied: LiveData<Boolean> = _filterApplied

    private val _isExpanded = MutableLiveData<Boolean>(false)
    val isExpanded: LiveData<Boolean> = _isExpanded

    private val _tierRestaurantList = MutableLiveData<List<TierRestaurant>>()
    val tierRestaurantList: LiveData<List<TierRestaurant>> = _tierRestaurantList

    private val _mapData = MutableLiveData<TierMapData>()
    val mapData: LiveData<TierMapData> = _mapData

    private val _selectedMenus = MutableLiveData<Set<String>>(setOf(""))
    val selectedMenus: LiveData<Set<String>> = _selectedMenus

    private val _selectedSituations = MutableLiveData<Set<String>>(setOf(""))
    val selectedSituations: LiveData<Set<String>> = _selectedSituations

    private val _selectedLocations = MutableLiveData<Set<String>>(setOf(""))
    val selectedLocations: LiveData<Set<String>> = _selectedLocations

    //티어 프래그먼트에서 보여줄 카테고리들
    private val _selectedCategories = MutableLiveData<Set<String>>()
    val selectedCategories: LiveData<Set<String>> = _selectedCategories

    private val _isSelectedCategoriesChanged = MutableLiveData<Boolean>(true)
    val isSelectedCategoriesChanged: LiveData<Boolean> = _isSelectedCategoriesChanged

    //음식점 리스트 페이지를 관리하는 변수
    private var _tierListPage = 1

    fun toggleExpand() {
        _isExpanded.value = _isExpanded.value?.not()
    }

    private fun loadRestaurantList(
        menus: Set<String>,
        situations: Set<String>,
        locations: Set<String>
    ) {
        viewModelScope.launch {
            val tierListData = getTierRestaurantListUseCase(
                CategoryIdMapper.mapMenus(menus),
                CategoryIdMapper.mapSituations(situations),
                CategoryIdMapper.mapLocations(locations),
                _tierListPage
            )

            _tierRestaurantList.value = tierListData.map {
                TierRestaurant(
                    restaurantId = it.restaurantId,
                    restaurantRanking = it.restaurantRanking?.toIntOrNull() ?: 0,
                    restaurantName = it.restaurantName,
                    restaurantCuisine = it.restaurantCuisine,
                    restaurantPosition = it.restaurantPosition,
                    restaurantImgUrl = it.restaurantImgUrl,
                    mainTier = it.mainTier,
                    partnershipInfo = it.partnershipInfo ?: "",
                    isFavorite = it.isFavorite,
                    x = it.x.toDouble(),
                    y = it.y.toDouble(),
                    isEvaluated = it.isEvaluated,
                    restaurantScore = it.restaurantScore?.toDoubleOrNull()?.takeIf { !it.isNaN() }
                        ?: 0.0
                )
            }
        }
    }

    private fun loadRestaurantMap(
        menus: Set<String>,
        situations: Set<String>,
        locations: Set<String>
    ) {
        viewModelScope.launch {
            val tierMapData = getTierRestaurantMapUseCase(
                CategoryIdMapper.mapMenus(menus),
                CategoryIdMapper.mapSituations(situations),
                CategoryIdMapper.mapLocations(locations)
            )
            _mapData.value = tierMapData
        }
    }

    private fun setSelectedTypes(types: Set<String>) {
        _selectedMenus.value = types
    }

    private fun setSelectedSituations(situations: Set<String>) {
        _selectedSituations.value = situations
    }

    private fun setSelectedLocations(locations: Set<String>) {
        _selectedLocations.value = locations
    }

    private fun setIsSelectedCategoriesChanged(flag: Boolean) {
        _isSelectedCategoriesChanged.value = flag
    }

    fun getLoadRestaurantMap() {
        loadRestaurantMap(
            selectedMenus.value ?: emptySet(),
            selectedSituations.value ?: emptySet(),
            selectedLocations.value ?: emptySet()
        )
    }

    fun applyFilters(
        menus: Set<String>,
        situations: Set<String>,
        locations: Set<String>,
        tabIndex: Int
    ) {
        if (hasFilterChanged(menus, situations, locations)) {
            _tierListPage = 1
            setIsSelectedCategoriesChanged(true)
            Log.e("TierViewModel", "applyFilters is called and hasFilter is True")
        } else {
            setIsSelectedCategoriesChanged(false)
            _tierListPage++
        }

        setSelectedTypes(menus)
        setSelectedSituations(situations)
        setSelectedLocations(locations)

        val selectedTypesValue = selectedMenus.value ?: emptySet()
        val selectedSelectedSituations = selectedSituations.value ?: emptySet()
        val selectedSelectedLocations = selectedLocations.value ?: emptySet()

        Log.e(
            "TierCategory",
            "TierViewModel에서" + "\n" + selectedTypesValue.toString() + "\n" + selectedSelectedSituations.toString() + "\n" + selectedSelectedLocations.toString() + "\b" + _isSelectedCategoriesChanged.value.toString()
        )

        if (tabIndex == 0) {
            loadRestaurantList(
                selectedTypesValue,
                selectedSelectedSituations,
                selectedSelectedLocations
            )
        } else if (tabIndex == 1) {
            loadRestaurantMap(
                selectedTypesValue,
                selectedSelectedSituations,
                selectedSelectedLocations
            )
        }

        updateSelectedCategories()
        _filterApplied.value = true
    }

    fun resetFilterApplied() {
        _filterApplied.value = false
    }

    fun checkAndLoadBackendListData(state: RestaurantState) {
        val selectedTypesValue = selectedMenus.value ?: emptySet()
        val selectedSelectedSituations = selectedSituations.value ?: emptySet()
        val selectedSelectedLocations = selectedLocations.value ?: emptySet()

        if (_isSelectedCategoriesChanged.value == true) {
            setIsSelectedCategoriesChanged(false)

            if (state == RestaurantState.RELOAD_RESTAURANT_LIST_DATA) {
                _tierListPage  = 1
            } else if (state == RestaurantState.NEXT_PAGE_LIST_DATA) {
                _tierListPage++
            }

            loadRestaurantList(
                selectedTypesValue,
                selectedSelectedSituations,
                selectedSelectedLocations
            )
        }
    }

    fun checkAndLoadBackendMapData() {
        val selectedTypesValue = selectedMenus.value ?: emptySet()
        val selectedSelectedSituations = selectedSituations.value ?: emptySet()
        val selectedSelectedLocations = selectedLocations.value ?: emptySet()

        if (_isSelectedCategoriesChanged.value == true) {
            setIsSelectedCategoriesChanged(false)

            loadRestaurantMap(
                selectedTypesValue,
                selectedSelectedSituations,
                selectedSelectedLocations
            )
        }
    }


    private fun updateSelectedCategories() {
        val selectedTypesValue = selectedMenus.value ?: emptySet()
        val selectedSelectedSituations = selectedSituations.value ?: emptySet()
        val selectedSelectedLocations = selectedLocations.value ?: emptySet()
        val categories = mutableSetOf<String>()

        if (selectedMenus.value != setOf("전체")) categories.addAll(selectedTypesValue)
        if (selectedSituations.value != setOf("전체")) categories.addAll(selectedSelectedSituations)
        if (selectedLocations.value != setOf("전체")) categories.addAll(selectedSelectedLocations)

        if (categories.isEmpty()) {
            categories.add("전체")
        }
        _selectedCategories.value = categories
    }

    fun hasFilterChanged(
        currentTypes: Set<String>,
        currentSituations: Set<String>,
        currentLocations: Set<String>
    ): Boolean {
        return (currentTypes != selectedMenus.value ||
                currentSituations != selectedSituations.value ||
                currentLocations != selectedLocations.value)
    }


    companion object {
        enum class RestaurantState {
            RELOAD_RESTAURANT_LIST_DATA,
            NEXT_PAGE_LIST_DATA
        }
    }
}

