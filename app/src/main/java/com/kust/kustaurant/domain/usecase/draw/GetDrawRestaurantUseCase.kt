package com.kust.kustaurant.domain.usecase.draw

import com.kust.kustaurant.data.model.DrawRestaurant
import com.kust.kustaurant.domain.repository.DrawRepository
import javax.inject.Inject

class GetDrawRestaurantUseCase @Inject constructor(
    private val drawRepository: DrawRepository) {
    suspend operator fun invoke(cuisines: String, locations: String):  List<DrawRestaurant> {
        return drawRepository.getDrawRestaurantData(cuisines, locations)
    }
}