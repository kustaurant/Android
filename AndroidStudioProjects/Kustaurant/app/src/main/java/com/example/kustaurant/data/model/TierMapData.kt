package com.example.kustaurant.data.model

import com.example.kustaurant.domain.model.Restaurant
import com.naver.maps.geometry.LatLng

data class TierListData(
    val polygonCoords: List<LatLng>,
    val solidLines: List<List<LatLng>>,
    val dashedLines: List<List<LatLng>>,
    val tieredRestaurants: List<Restaurant>,
    val nonTieredRestaurants: List<NonTieredRestaurantGroup>,
    val minZoom: Int
)