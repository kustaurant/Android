package com.kust.kustaurant.data.repository

import com.kust.kustaurant.data.mapper.toTierMapData
import com.kust.kustaurant.data.model.RestaurantResponse
import com.kust.kustaurant.data.model.TierMapData
import com.kust.kustaurant.data.remote.KustaurantApi
import com.kust.kustaurant.domain.repository.TierRepository
import javax.inject.Inject

class TierRepositoryImpl @Inject constructor(
    private val kustaurantApi: KustaurantApi
) : TierRepository {
    override suspend fun getRestaurantList(cuisines: String, situations: String, locations: String): List<RestaurantResponse> {
        return kustaurantApi.getRestaurantList(cuisines, situations, locations)
    }

    override suspend fun getRestaurantMapList(cuisines: String, situations: String, locations: String): TierMapData {
        return kustaurantApi.getTierMapList(cuisines, situations, locations).toTierMapData()
    }
}