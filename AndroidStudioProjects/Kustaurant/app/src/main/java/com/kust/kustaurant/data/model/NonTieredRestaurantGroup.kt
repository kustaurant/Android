package com.kust.kustaurant.data.model

import com.kust.kustaurant.domain.model.TierRestaurant

data class NonTieredRestaurantGroup(
    val zoom: Int,
    val tierRestaurants: List<TierRestaurant>
)
