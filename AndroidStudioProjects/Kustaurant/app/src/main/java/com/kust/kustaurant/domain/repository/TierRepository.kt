package com.kust.kustaurant.domain.repository

import com.kust.kustaurant.data.model.RestaurantResponse
import com.kust.kustaurant.data.model.TierMapData

interface TierRepository {
    suspend fun getRestaurantList(cuisines: String, situations: String, locations: String): List<RestaurantResponse>
    suspend fun getRestaurantMapList(cuisines: String, situations: String, locations: String): TierMapData
}
