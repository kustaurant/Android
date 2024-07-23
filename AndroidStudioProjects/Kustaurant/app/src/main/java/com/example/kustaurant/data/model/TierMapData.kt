package com.example.kustaurant.data.model

import com.naver.maps.geometry.LatLng

data class TierMapData(
    val polygonCoords: List<LatLng>,
    val solidLines: List<List<LatLng>>,
    val dashedLines: List<List<LatLng>>,
    val tieredRestaurants: List<Restaurant>,
    val nonTieredRestaurants: List<NonTieredRestaurantGroup>,
    val minZoom: Int
)