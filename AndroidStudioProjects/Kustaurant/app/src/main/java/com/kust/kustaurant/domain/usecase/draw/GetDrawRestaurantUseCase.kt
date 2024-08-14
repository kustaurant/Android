package com.kust.kustaurant.domain.usecase.draw

import com.kust.kustaurant.data.model.DrawRestaurantData
import com.kust.kustaurant.domain.repository.DrawRepository
import javax.inject.Inject

class GetDrawRestaurantUseCase @Inject constructor(
    private val drawRepository: DrawRepository) {
    suspend operator fun invoke(cuisines: String, situations: String, locations: String):  List<DrawRestaurantData> {
        return drawRepository.getDrawRestaurantData(cuisines, situations, locations)
    }
}