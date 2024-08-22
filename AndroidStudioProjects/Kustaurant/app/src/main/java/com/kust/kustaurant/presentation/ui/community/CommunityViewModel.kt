package com.kust.kustaurant.presentation.ui.community

import androidx.lifecycle.ViewModel
import com.kust.kustaurant.domain.usecase.tier.GetTierRestaurantListUseCase
import com.kust.kustaurant.domain.usecase.tier.GetTierRestaurantMapUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val getTierRestaurantListUseCase: GetTierRestaurantListUseCase,
    private val getTierRestaurantMapUseCase: GetTierRestaurantMapUseCase
) : ViewModel() {

    init {

    }
}

