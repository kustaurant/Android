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
    private var _sameDrawList = 0 // limit 3 times + manage first draw
    private val _selectedMenus = MutableLiveData(setOf("전체"))
    val selectedMenus: LiveData<Set<String>> = _selectedMenus

    private val _selectedLocations = MutableLiveData(setOf("전체"))
    val selectedLocations: LiveData<Set<String>> = _selectedLocations

    private var _previousSelectedMenus = setOf("")
    private var _previousSelectedLocations = setOf("")

    private val _selectedRestaurant = MutableLiveData<DrawRestaurantData>()
    val selectedRestaurant: LiveData<DrawRestaurantData> = _selectedRestaurant

    fun drawRestaurants() {
        viewModelScope.launch {
            isResetFilter()

            if (_sameDrawList < 2) {
                _sameDrawList++
                _drawList.value?.let { currentList ->
                    if (currentList.isNotEmpty()) {
                        val selected = getRandomRestaurants(currentList)
                        _selectedRestaurant.value = selected
                        updateSelectedIndex(currentList, selected)
                    }
                }
            } else {
                _sameDrawList = 0

                val mappedMenus = selectedMenus.value?.let { CategoryIdMapper.mapMenus(it) }
                val mappedLocations = selectedLocations.value?.let { CategoryIdMapper.mapLocations(it) }

                if (mappedMenus != null && mappedLocations != null) {
                    val drawRestaurantsListData = getDrawRestaurantUseCase(
                        mappedMenus,
                        mappedLocations
                    )
                    _drawList.value = drawRestaurantsListData.shuffled().take(20)

                    val selected = getRandomRestaurants(_drawList.value!!)
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
    }

    private fun isResetFilter() {
        if(!((selectedMenus.value == _previousSelectedMenus) &&
                (selectedLocations.value == _previousSelectedLocations))) {
            _sameDrawList = 3
            _previousSelectedMenus = selectedMenus.value!!
            _previousSelectedLocations = selectedLocations.value!!
        }
    }
}

