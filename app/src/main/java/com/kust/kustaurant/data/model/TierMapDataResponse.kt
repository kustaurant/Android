package com.kust.kustaurant.data.model

data class TierMapDataResponse(
    val solidPolygonCoordsList: List<List<LatLngResponse>>,
    val dashedPolygonCoordsList: List<List<LatLngResponse>>,
    val favoriteTierRestaurants : List<RestaurantResponse>,
    val tieredRestaurants: List<RestaurantResponse>,
    val nonTieredRestaurants: List<NonTieredRestaurantResponse>,
    val minZoom: Int
)