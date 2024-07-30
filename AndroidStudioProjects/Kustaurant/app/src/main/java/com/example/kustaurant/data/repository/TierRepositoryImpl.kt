package com.example.kustaurant.data.repository

import com.example.kustaurant.data.mapper.toTierMapData
import com.example.kustaurant.data.model.TierListData
import com.example.kustaurant.data.remote.MapApi
import com.example.kustaurant.domain.repository.TierRepository
import javax.inject.Inject

class TierRepositoryImpl @Inject constructor(
    private val mapApi: MapApi
) : TierRepository {
    override suspend fun getTierListData(cuisines: String, situations: String, locations: String): TierListData {
        return mapApi.getTierCategoryData(cuisines, situations, locations).toTierMapData()
    }
}