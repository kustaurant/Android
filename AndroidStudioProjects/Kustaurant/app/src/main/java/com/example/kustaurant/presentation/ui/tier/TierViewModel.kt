package com.example.kustaurant.presentation.ui.tier

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kustaurant.TierModel
import com.example.kustaurant.data.model.TierListData
import com.example.kustaurant.domain.usecase.GetTierListDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class TierViewModel @Inject constructor(
    private val getTierListDataUseCase: GetTierListDataUseCase
) : ViewModel() {

    private val _isExpanded = MutableLiveData<Boolean>(false)
    val isExpanded: LiveData<Boolean> = _isExpanded

    private val _tierList = MutableLiveData<List<TierModel>>()
    val tierList: LiveData<List<TierModel>> = _tierList

    private val _mapData = MutableLiveData<TierListData>()
    val mapData: LiveData<TierListData> = _mapData

    // LiveData for storing selected filters
    private val _selectedTypes = MutableLiveData<Set<String>>(setOf("전체"))
    val selectedTypes: LiveData<Set<String>> = _selectedTypes

    private val _selectedSituations = MutableLiveData<Set<String>>(setOf("전체"))
    val selectedSituations: LiveData<Set<String>> = _selectedSituations

    private val _selectedLocations = MutableLiveData<Set<String>>(setOf("전체"))
    val selectedLocations: LiveData<Set<String>> = _selectedLocations

    private var _initialSelectedTypes = setOf("전체")
    private var _initialSelectedSituations = setOf("전체")
    private var _initialSelectedLocations = setOf("전체")

    init {
        loadTierList("전체", "전체", "전체")
    }

    fun toggleExpand() {
        _isExpanded.value = _isExpanded.value?.not()
    }

    fun loadTierList(cuisines: String, situations: String, locations: String) {

        viewModelScope.launch {
            val encodedCuisines = Uri.encode(cuisines)
            val encodedSituations = Uri.encode(situations)
            val encodedLocations = Uri.encode(locations)
            val tierListData = getTierListDataUseCase(encodedCuisines, encodedSituations, encodedLocations)
            _mapData.value = tierListData
            _tierList.value = tierListData.tieredRestaurants.map {
                TierModel(
                    restaurantId = it.restaurantId,
                    restaurantRanking = it.restaurantRanking,
                    restaurantName = it.restaurantName,
                    restaurantCuisine = it.restaurantCuisine,
                    restaurantPosition = it.restaurantPosition,
                    restaurantImgUrl = it.restaurantImgUrl,
                    mainTier = it.mainTier,
                    partnershipInfo = it.partnershipInfo,
                    isFavorite = it.isFavorite,
                    x = it.x,
                    y = it.y,
                    isEvaluated = it.isEvaluated,
                )
            }
        }
    }

    fun setSelectedTypes(types: Set<String>) {
        _selectedTypes.value = types
    }

    fun setSelectedSituations(situations: Set<String>) {
        _selectedSituations.value = situations
    }

    fun setSelectedLocations(locations: Set<String>) {
        _selectedLocations.value = locations
    }

    fun applyFilters(types: Set<String>, situations: Set<String>, locations: Set<String>) {
        setSelectedTypes(types)
        setSelectedSituations(situations)
        setSelectedLocations(locations)

        // 현재 선택된 값들을 새로운 "초기" 값으로 설정
        _initialSelectedTypes = types
        _initialSelectedSituations = situations
        _initialSelectedLocations = locations

        loadTierList(
            types.joinToString(", "),
            situations.joinToString(", "),
            locations.joinToString(", ")
        )
    }

    fun hasFilterChanged(currentTypes: Set<String>, currentSituations: Set<String>, currentLocations: Set<String>): Boolean {
        return (currentTypes != _initialSelectedTypes ||
                currentSituations != _initialSelectedSituations ||
                currentLocations != _initialSelectedLocations)
    }
}

