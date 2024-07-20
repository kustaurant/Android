package com.example.kustaurant.domain.repository

import com.example.kustaurant.presentation.ui.tier.TierMapData

interface MapRepository {
    suspend fun getTierMapData(cuisines: String, situations: String, locations: String): TierMapData
}
