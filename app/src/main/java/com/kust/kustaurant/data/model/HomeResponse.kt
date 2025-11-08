package com.kust.kustaurant.data.model

import com.kust.kustaurant.data.model.tier.RestaurantResponse

data class HomeResponse(
    val topRestaurantsByRating: List<RestaurantResponse>,
    val restaurantsForMe: List<RestaurantResponse>,
    val photoUrls: List<String>
)