package com.kust.kustaurant.data.model.tier

import com.kust.kustaurant.data.model.LatLngResponse

data class TierMapDataResponse(
    val minZoom: Int,
    val favoriteTierRestaurants : List<RestaurantResponse>,
    val tieredRestaurants: List<RestaurantResponse>,
    val nonTieredRestaurants: List<NonTieredRestaurantResponse>,
    val solidPolygonCoordsList: List<List<LatLngResponse>>,
    val dashedPolygonCoordsList: List<List<LatLngResponse>>,
    val visibleBounds : List<Double>
)