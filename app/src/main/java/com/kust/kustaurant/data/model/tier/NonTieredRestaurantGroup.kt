package com.kust.kustaurant.data.model.tier

import com.kust.kustaurant.domain.model.TierRestaurant

data class NonTieredRestaurantGroup(
    val zoom: Int,
    val tierRestaurants: List<TierRestaurant>
)
