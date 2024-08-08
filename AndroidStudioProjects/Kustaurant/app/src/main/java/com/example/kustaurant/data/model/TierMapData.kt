package com.example.kustaurant.data.model

import com.example.kustaurant.domain.model.TierRestaurant
import com.naver.maps.geometry.LatLng

data class TierMapData(
    val polygonCoords: List<LatLng>,
    val solidLines: List<List<LatLng>>,
    val dashedLines: List<List<LatLng>>,
    val favoriteTierRestaurants : List<TierRestaurant>,
    val tieredTierRestaurants: List<TierRestaurant>,
    val nonTieredRestaurants: List<NonTieredRestaurantGroup>,
    val minZoom: Int
)