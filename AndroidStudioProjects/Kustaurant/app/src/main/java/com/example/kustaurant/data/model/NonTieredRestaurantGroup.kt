package com.example.kustaurant.data.model

import com.example.kustaurant.domain.model.Restaurant

data class NonTieredRestaurantGroup(
    val zoom: Int,
    val restaurants: List<Restaurant>
)
