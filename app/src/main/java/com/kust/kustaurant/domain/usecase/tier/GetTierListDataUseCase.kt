package com.kust.kustaurant.domain.usecase.tier

import com.kust.kustaurant.data.model.RestaurantResponse
import com.kust.kustaurant.domain.repository.TierRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetTierRestaurantListUseCase @Inject constructor(
    private val tierRepository: TierRepository
) {
    suspend operator fun invoke(cuisines: String, situations: String, locations: String, page : Int): List<RestaurantResponse> {
        return tierRepository.getRestaurantList(cuisines, situations, locations, page)
    }
}
