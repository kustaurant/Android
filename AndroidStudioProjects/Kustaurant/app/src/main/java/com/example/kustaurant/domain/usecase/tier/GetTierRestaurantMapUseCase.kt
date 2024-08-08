package com.example.kustaurant.domain.usecase.tier

import com.example.kustaurant.data.model.TierMapData
import com.example.kustaurant.domain.repository.TierRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetTierRestaurantMapUseCase @Inject constructor(
    private val tierRepository: TierRepository
) {
    suspend operator fun invoke(cuisines: String, situations: String, locations: String): TierMapData {
        return tierRepository.getRestaurantMapList(cuisines, situations, locations)
    }
}
