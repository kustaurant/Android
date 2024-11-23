package com.kust.kustaurant.presentation.ui.draw

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kust.kustaurant.data.model.DrawRestaurantData
import com.kust.kustaurant.domain.usecase.draw.GetDrawRestaurantUseCase
import com.kust.kustaurant.presentation.util.CategoryIdMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DrawViewModel @Inject constructor(
    private val getDrawRestaurantUseCase: GetDrawRestaurantUseCase
) : ViewModel() {
    private val _drawList = MutableLiveData<List<DrawRestaurantData>>()
    val drawList: LiveData<List<DrawRestaurantData>> = _drawList
    private var sameDrawList = 3
    private val _selectedMenus = MutableLiveData(setOf("전체"))
    val selectedMenus: LiveData<Set<String>> = _selectedMenus

    private val _selectedLocations = MutableLiveData(setOf("전체"))
    val selectedLocations: LiveData<Set<String>> = _selectedLocations

    private var _initialSelectedMenus = setOf("전체")
    private var _initialSelectedLocations = setOf("전체")

    private val _selectedRestaurant = MutableLiveData<DrawRestaurantData>()
    val selectedRestaurant: LiveData<DrawRestaurantData> = _selectedRestaurant

    fun drawRestaurants() {
        viewModelScope.launch {
            val checkSameCategory = (selectedMenus.value == _initialSelectedMenus) &&
                    (selectedLocations.value == _initialSelectedLocations)

            if (checkSameCategory && sameDrawList < 2) {
                sameDrawList++
                _drawList.value?.let { currentList ->
                    if (currentList.isNotEmpty()) {
                        val selected = getRandomRestaurants(currentList)
                        _selectedRestaurant.value = selected
                        updateSelectedIndex(currentList, selected)
                    }
                }
            } else {
                sameDrawList = 0

                val mappedMenus = selectedMenus.value?.let { CategoryIdMapper.mapMenus(it) }
                val mappedLocations = selectedLocations.value?.let { CategoryIdMapper.mapLocations(it) }

                if (mappedMenus != null && mappedLocations != null) {
                    Log.d("DrawViewModel", "$mappedMenus $mappedLocations")
                    val drawRestaurantsListData = getDrawRestaurantUseCase(
                        mappedMenus,
                        mappedLocations
                    )
                    _drawList.value = drawRestaurantsListData

                    val selected = getRandomRestaurants(drawRestaurantsListData)
                    _selectedRestaurant.value = selected
                    updateSelectedIndex(drawRestaurantsListData, selected)
                } else {
                    Log.e("DrawViewModel", "Menus or Locations mapping failed. Menus: $mappedMenus, Locations: $mappedLocations")
                }
            }
        }
    }

    private var _selectedIndex = MutableLiveData<Int>()
    val selectedIndex: LiveData<Int> = _selectedIndex

    private fun updateSelectedIndex(restaurants: List<DrawRestaurantData>, selected: DrawRestaurantData) {
        _selectedIndex.value = restaurants.indexOf(selected)
    }

    private fun getRandomRestaurants(restaurants: List<DrawRestaurantData>): DrawRestaurantData {
        return restaurants.shuffled().first()
    }

    private fun setSelectedTypes(types: Set<String>) {
        _selectedMenus.value = types
    }

    private fun setSelectedLocations(locations: Set<String>) {
        _selectedLocations.value = locations
    }

    fun applyFilters(types: Set<String>, locations: Set<String>) {
        setSelectedTypes(types)
        setSelectedLocations(locations)

        // 현재 선택된 값들을 새로운 "초기" 값으로 설정
        _initialSelectedMenus = types
        _initialSelectedLocations = locations

        sameDrawList = 3
    }
}

