package com.kust.kustaurant.domain.usecase.tier

import com.kust.kustaurant.data.model.tier.TierMapData
import com.kust.kustaurant.domain.repository.TierRepository
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
