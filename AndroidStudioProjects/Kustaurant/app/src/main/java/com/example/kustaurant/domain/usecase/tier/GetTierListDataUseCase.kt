package com.example.kustaurant.domain.usecase.tier

import com.example.kustaurant.data.model.RestaurantResponse
import com.example.kustaurant.domain.repository.TierRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetTierRestaurantListUseCase @Inject constructor(
    private val tierRepository: TierRepository
) {
    suspend operator fun invoke(cuisines: String, situations: String, locations: String): List<RestaurantResponse> {
        return tierRepository.getRestaurantList(cuisines, situations, locations)
    }
}
