package com.kust.kustaurant.data.model.tier

data class NonTieredRestaurantResponse(
    val zoom: Int,
    val restaurants: List<RestaurantResponse>
)
