package com.kust.kustaurant.data.repository

import com.kust.kustaurant.data.mapper.toTierMapData
import com.kust.kustaurant.data.model.RestaurantResponse
import com.kust.kustaurant.data.model.TierMapData
import com.kust.kustaurant.data.remote.TierApi
import com.kust.kustaurant.domain.repository.TierRepository
import javax.inject.Inject

class TierRepositoryImpl @Inject constructor(
    private val tierApi: TierApi
) : TierRepository {
    override suspend fun getRestaurantList(
        cuisines: String,
        situations: String,
        locations: String,
        page: Int
    ): List<RestaurantResponse> {
        return tierApi.getRestaurantList(cuisines, situations, locations, page)
    }

    override suspend fun getRestaurantMapList(
        cuisines: String,
        situations: String,
        locations: String
    ): TierMapData {
        return tierApi.getTierMapList(cuisines, situations, locations).toTierMapData()
    }
}