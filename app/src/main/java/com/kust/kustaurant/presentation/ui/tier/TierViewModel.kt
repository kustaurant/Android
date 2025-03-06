package com.kust.kustaurant.presentation.ui.tier

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kust.kustaurant.data.model.TierMapData
import com.kust.kustaurant.domain.model.TierRestaurant
import com.kust.kustaurant.domain.usecase.tier.GetAuthTierRestaurantListUseCase
import com.kust.kustaurant.domain.usecase.tier.GetAuthTierRestaurantMapUseCase
import com.kust.kustaurant.domain.usecase.tier.GetTierRestaurantListUseCase
import com.kust.kustaurant.domain.usecase.tier.GetTierRestaurantMapUseCase
import com.kust.kustaurant.presentation.common.CategoryIdMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TierViewModel @Inject constructor(
    private val getTierRestaurantListUseCase: GetTierRestaurantListUseCase,
    private val getTierRestaurantMapUseCase: GetTierRestaurantMapUseCase,
    private val getAuthTierRestaurantListUseCase: GetAuthTierRestaurantListUseCase,
    private val getAuthTierRestaurantMapUseCase: GetAuthTierRestaurantMapUseCase
) : ViewModel(){
    private val _isExpanded = MutableLiveData(false)
    val isExpanded: LiveData<Boolean> = _isExpanded

    private val _allRestaurants = MutableLiveData<List<TierRestaurant>>(emptyList())
    val allRestaurants: LiveData<List<TierRestaurant>> = _allRestaurants

    private val _fetchedRestaurants = MutableLiveData<List<TierRestaurant>>()
    val fetchedRestaurants: LiveData<List<TierRestaurant>> = _fetchedRestaurants

    private val _mapData = MutableLiveData<TierMapData>()
    val mapData: LiveData<TierMapData> = _mapData

    private val _selectedMenus = MutableLiveData(setOf(""))
    val selectedMenus: LiveData<Set<String>> = _selectedMenus

    private val _selectedSituations = MutableLiveData(setOf(""))
    val selectedSituations: LiveData<Set<String>> = _selectedSituations

    private val _selectedLocations = MutableLiveData(setOf(""))
    val selectedLocations: LiveData<Set<String>> = _selectedLocations

    // 티어 프래그먼트에서 보여줄 카테고리들
    private val _selectedCategories = MutableLiveData<Set<String>>()
    val selectedCategories: LiveData<Set<String>> = _selectedCategories

    private val _categoryChangeList = MutableLiveData(true)
    val categoryChangeList: LiveData<Boolean> = _categoryChangeList

    private val _categoryChangeMap = MutableLiveData(true)

    private val _isShowBottomSheet = MutableLiveData<Boolean>(false)
    val isShowBottomSheet: LiveData<Boolean> get() = _isShowBottomSheet

    // 음식점 리스트(TierListFragment) 페이지
    private var _tierListPage = 1
    // 음식점 리스트(TierListFragment) 마지막 스크롤 위치
    private var _tierListLastPosition: Int = 0

    private fun loadRestaurantList(isAuth : Boolean) {
        val menus = selectedMenus.value ?: setOf("전체")
        val situations = selectedSituations.value ?: setOf("전체")
        val locations = selectedLocations.value ?: setOf("전체")

        var flagJH = false
        /*
         * 정책 사항 : 제휴업체 대상인 경우 티어 이미지를 노출하지 않도록 한다.
         */
        if (menus.elementAt(0) == "제휴업체")
            flagJH = true

        viewModelScope.launch {
            try {
                val tierListData = if(isAuth) {
                    getAuthTierRestaurantListUseCase(
                        CategoryIdMapper.mapMenus(menus),
                        CategoryIdMapper.mapSituations(situations),
                        CategoryIdMapper.mapLocations(locations),
                        _tierListPage
                    )
                } else {
                    getTierRestaurantListUseCase(
                        CategoryIdMapper.mapMenus(menus),
                        CategoryIdMapper.mapSituations(situations),
                        CategoryIdMapper.mapLocations(locations),
                        _tierListPage
                    )
                }

                val fetchedData = tierListData.map { it ->
                    TierRestaurant(
                        restaurantId = it.restaurantId,
                        restaurantRanking = it.restaurantRanking?.toIntOrNull() ?: 0,
                        restaurantName = it.restaurantName,
                        restaurantCuisine = it.restaurantCuisine,
                        restaurantPosition = it.restaurantPosition,
                        restaurantImgUrl = it.restaurantImgUrl,
                        mainTier = if (flagJH) -1 else it.mainTier,
                        partnershipInfo = it.partnershipInfo ?: "",
                        isFavorite = it.isFavorite,
                        x = it.x.toDouble(),
                        y = it.y.toDouble(),
                        isEvaluated = it.isEvaluated,
                        restaurantScore = it.restaurantScore?.toDoubleOrNull()
                            ?.takeIf { !it.isNaN() }
                            ?: 0.0
                    )
                }
                _fetchedRestaurants.value = fetchedData

                val currentAllRestaurants = _allRestaurants.value ?: emptyList()
                _allRestaurants.value = currentAllRestaurants + fetchedData
            } catch (e: Exception) {
                Log.e("TierViewModel", "From loadRestaurantList, Err is $e")
            }
        }
    }

    private fun loadRestaurantMap(isAuth: Boolean) {
        val menus = selectedMenus.value ?: setOf("전체")
        val situations = selectedSituations.value ?: setOf("전체")
        val locations = selectedLocations.value ?: setOf("전체")

        viewModelScope.launch {
            try {
                val tierMapData = if(isAuth) {
                    getAuthTierRestaurantMapUseCase(
                        CategoryIdMapper.mapMenus(menus),
                        CategoryIdMapper.mapSituations(situations),
                        CategoryIdMapper.mapLocations(locations)
                    )
                } else {
                    getTierRestaurantMapUseCase(
                        CategoryIdMapper.mapMenus(menus),
                        CategoryIdMapper.mapSituations(situations),
                        CategoryIdMapper.mapLocations(locations)
                    )
                }
                _mapData.value = tierMapData
            } catch (e: Exception) {
                Log.e("TierViewModel", "From loadRestaurantMap, Err is $e")
            }
        }
    }

    fun toggleExpand() {
        _isExpanded.value = _isExpanded.value?.not()
    }

    private fun setSelectedMenus(menus: Set<String>) {
        _selectedMenus.value = menus
    }

    private fun setSelectedSituations(situations: Set<String>) {
        _selectedSituations.value = situations
    }

    private fun setSelectedLocations(locations: Set<String>) {
        _selectedLocations.value = locations
    }


    fun setCategory(
        menus: Set<String>,
        situations: Set<String>,
        locations: Set<String>
    ) {
        setSelectedMenus(menus)
        setSelectedSituations(situations)
        setSelectedLocations(locations)
        _tierListPage = 1
        updateFilter()
        _categoryChangeList.value = true
        _categoryChangeMap.value = true
    }

    fun loadRestaurant(
        screenType: TierScreenType,
        isAuth: Boolean,
    ) {
        if (screenType == TierScreenType.LIST) {
            loadRestaurantList(isAuth)
        } else if (screenType == TierScreenType.MAP) {
            loadRestaurantMap(isAuth)
        }
        updateFilter()
    }

    private fun updateFilter() {
        val categories = mutableSetOf<String>()

        if (selectedMenus.value != setOf("전체"))
            selectedMenus.value?.let { categories.addAll(it) }
        if (selectedSituations.value != setOf("전체"))
            selectedSituations.value?.let { categories.addAll(it) }
        if (selectedLocations.value != setOf("전체"))
            selectedLocations.value?.let { categories.addAll(it) }
        if (categories.isEmpty())
            categories.add("전체")

        _selectedCategories.value = categories
    }

    fun fetchFirstRestaurants(isAuth: Boolean) {
        _allRestaurants.value = emptyList()
        _categoryChangeList.value = false
        _tierListPage = 1
        loadRestaurantList(isAuth)
    }

    fun fetchNextRestaurants(isAuth: Boolean) {
        _tierListPage++
        loadRestaurantList(isAuth)
    }

    fun getTierListLastPosition(): Int {
        return _tierListLastPosition
    }

    fun setTierListLastPosition(position: Int) {
        _tierListLastPosition = position
    }
    fun setShowBottomSheet(show: Boolean) {
        _isShowBottomSheet.value = show
    }
}