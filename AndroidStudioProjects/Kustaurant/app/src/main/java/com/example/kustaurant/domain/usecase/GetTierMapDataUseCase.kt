package com.example.kustaurant.domain.usecase

import com.example.kustaurant.data.model.TierMapData
import com.example.kustaurant.domain.repository.MapRepository
import javax.inject.Inject

class GetTierMapDataUseCase @Inject constructor(
    private val mapRepository: MapRepository
) {
    suspend operator fun invoke(cuisines: String, situations: String, locations: String): TierMapData {
        return mapRepository.getTierMapData(cuisines, situations, locations)
    }
}
