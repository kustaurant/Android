package com.kust.kustaurant.presentation.ui.tier

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kust.kustaurant.data.datasource.AuthPreferenceDataSource
import com.kust.kustaurant.data.model.tier.TierMapData
import com.kust.kustaurant.domain.model.TierRestaurant
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
    private val prefs: AuthPreferenceDataSource
) : ViewModel(){
    private val _isExpanded = MutableLiveData(false)
    val isExpanded: LiveData<Boolean> = _isExpanded

    private val _allRestaurants = MutableLiveData<List<TierRestaurant>>(emptyList())
    val allRestaurants: LiveData<List<TierRestaurant>> = _allRestaurants

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

    // 음식점 리스트(TierListFragment) 마지막 스크롤 위치
    private var _tierListLastPosition: Int = 0

    private val _pageState = MutableLiveData(TierPageState())
    val pageState: LiveData<TierPageState> = _pageState

    private fun loadRestaurantList(requestedPage: Int) {
        val state = _pageState.value ?: return
        if (state.phase != TierPhase.Refreshing && state.phase != TierPhase.Paging) return

        val menus = selectedMenus.value ?: setOf("전체")
        val situations = selectedSituations.value ?: setOf("전체")
        val locations = selectedLocations.value ?: setOf("전체")
        val isPartnership = menus.contains("제휴업체")

        viewModelScope.launch {
            try {
                val tierListData = getTierRestaurantListUseCase(
                    CategoryIdMapper.mapMenus(menus),
                    CategoryIdMapper.mapSituations(situations),
                    CategoryIdMapper.mapLocations(locations),
                    requestedPage
                )

                val fetched = tierListData.map {
                    TierRestaurant(
                        restaurantId = it.restaurantId,
                        restaurantRanking = it.restaurantRanking ?: 0,
                        restaurantName = it.restaurantName,
                        restaurantCuisine = it.restaurantCuisine,
                        restaurantPosition = it.restaurantPosition,
                        restaurantImgUrl = it.restaurantImgUrl,
                        mainTier = if (isPartnership) -1 else it.mainTier,
                        partnershipInfo = it.partnershipInfo ?: "",
                        isFavorite = it.isFavorite,
                        x = it.longitude,
                        y = it.latitude,
                        isEvaluated = it.isEvaluated,
                        restaurantScore = it.restaurantScore?.takeIf { s -> !s.isNaN() } ?: 0.0
                    )
                }

                val last = fetched.isEmpty()
                val current = _allRestaurants.value ?: emptyList()

                _allRestaurants.value =
                    if (requestedPage == 1) fetched else current + fetched

                _pageState.value = state.copy(
                    phase = TierPhase.Idle,
                    page = requestedPage,
                    isLastPage = last
                )
            } catch (e: Exception) {
                Log.e("TierViewModel", "loadRestaurantList error: $e")
                _pageState.value = state.copy(phase = TierPhase.Idle)
            }
        }
    }

    private fun loadRestaurantMap() {
        val menus = selectedMenus.value ?: setOf("전체")
        val situations = selectedSituations.value ?: setOf("전체")
        val locations = selectedLocations.value ?: setOf("전체")

        viewModelScope.launch {
            try {
                val tierMapData =
                    getTierRestaurantMapUseCase(
                        CategoryIdMapper.mapMenus(menus),
                        CategoryIdMapper.mapSituations(situations),
                        CategoryIdMapper.mapLocations(locations)
                    )
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

        if( _pageState.value == null) return
        _pageState.value = _pageState.value!!.copy(page = 1)

        updateFilter()
        _categoryChangeList.value = true
        _categoryChangeMap.value = true
    }

    fun loadRestaurant(
        screenType: TierScreenType,
    ) {
        if (screenType == TierScreenType.LIST) {
            fetchFirstRestaurants()
        } else if (screenType == TierScreenType.MAP) {
            loadRestaurantMap()
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

    fun fetchFirstRestaurants() {
        if( _pageState.value == null) return

        _allRestaurants.value = emptyList()
        _categoryChangeList.value = false
        _pageState.value = _pageState.value!!.copy(phase = TierPhase.Refreshing, page = 1, isLastPage = false)
        loadRestaurantList(1)
    }

    fun fetchNextRestaurants() {
        if( _pageState.value == null) return

        val s = _pageState.value!!
        if (s.phase != TierPhase.Idle || s.isLastPage) return
        _pageState.value = s.copy(phase = TierPhase.Paging)
        loadRestaurantList(s.page + 1)
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

    fun hasLoginInfo(): Boolean {
        prefs.getAccessToken()?.let {
            return true
        }
        return false
    }
}