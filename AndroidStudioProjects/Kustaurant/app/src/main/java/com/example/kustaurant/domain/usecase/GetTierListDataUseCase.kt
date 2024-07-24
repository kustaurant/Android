package com.example.kustaurant.domain.usecase

import com.example.kustaurant.data.model.TierListData
import com.example.kustaurant.domain.repository.TierRepository
import javax.inject.Inject

class GetTierListDataUseCase @Inject constructor(
    private val tierRepository: TierRepository
) {
    suspend operator fun invoke(cuisines: String, situations: String, locations: String): TierListData {
        return tierRepository.getTierListData(cuisines, situations, locations)
    }
}
