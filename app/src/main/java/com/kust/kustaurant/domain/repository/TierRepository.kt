package com.kust.kustaurant.domain.repository

import com.kust.kustaurant.data.model.tier.RestaurantResponse
import com.kust.kustaurant.data.model.tier.TierMapData

interface TierRepository {
    suspend fun getRestaurantList(cuisines: String, situations: String, locations: String, page : Int): List<RestaurantResponse>
    suspend fun getRestaurantMapList(cuisines: String, situations: String, locations: String): TierMapData
}
