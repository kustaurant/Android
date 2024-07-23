package com.example.kustaurant.domain.repository

import com.example.kustaurant.data.model.TierMapData


interface MapRepository {
    suspend fun getTierMapData(cuisines: String, situations: String, locations: String): TierMapData
}
