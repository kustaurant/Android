package com.example.kustaurant.data.model

import com.example.kustaurant.domain.model.TierRestaurant

data class NonTieredRestaurantGroup(
    val zoom: Int,
    val tierRestaurants: List<TierRestaurant>
)
