package com.kust.kustaurant.data.model

data class NonTieredRestaurantResponse(
    val zoom: Int,
    val restaurants: List<RestaurantResponse>
)
