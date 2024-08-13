package com.example.kustaurant.domain.repository

import com.example.kustaurant.data.model.RestaurantResponse
import com.example.kustaurant.data.model.TierMapData

interface TierRepository {
    suspend fun getRestaurantList(cuisines: String, situations: String, locations: String): List<RestaurantResponse>
    suspend fun getRestaurantMapList(cuisines: String, situations: String, locations: String): TierMapData
}
