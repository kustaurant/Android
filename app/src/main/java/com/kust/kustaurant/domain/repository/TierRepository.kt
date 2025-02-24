package com.kust.kustaurant.domain.repository

import com.kust.kustaurant.data.model.RestaurantResponse
import com.kust.kustaurant.data.model.TierMapData

interface TierRepository {
    suspend fun getRestaurantList(cuisines: String, situations: String, locations: String, page : Int): List<RestaurantResponse>
    suspend fun getRestaurantMapList(cuisines: String, situations: String, locations: String): TierMapData
    suspend fun getAuthRestaurantList(cuisines: String, situations: String, locations: String, page : Int): List<RestaurantResponse>
    suspend fun getAuthRestaurantMapList(cuisines: String, situations: String, locations: String): TierMapData
}
