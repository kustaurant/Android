package com.example.kustaurant.data.repository

import com.example.kustaurant.data.mapper.toTierMapData
import com.example.kustaurant.data.remote.MapApi
import com.example.kustaurant.domain.repository.MapRepository
import com.example.kustaurant.presentation.ui.tier.TierMapData
import javax.inject.Inject

class MapRepositoryImpl @Inject constructor(
    private val mapApi: MapApi
) : MapRepository {
    override suspend fun getTierMapData(cuisines: String, situations: String, locations: String): TierMapData {
        return mapApi.getTierMapData(cuisines, situations, locations).toTierMapData()
    }
}