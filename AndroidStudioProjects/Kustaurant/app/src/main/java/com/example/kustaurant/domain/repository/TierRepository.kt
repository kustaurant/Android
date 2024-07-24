package com.example.kustaurant.domain.repository

import com.example.kustaurant.data.model.TierListData


interface TierRepository {
    suspend fun getTierListData(cuisines: String, situations: String, locations: String): TierListData
}
