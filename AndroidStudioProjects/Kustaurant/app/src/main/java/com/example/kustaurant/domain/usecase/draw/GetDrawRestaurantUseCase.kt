package com.example.kustaurant.domain.usecase.draw

import com.example.kustaurant.data.model.DrawRestaurantData
import com.example.kustaurant.domain.repository.DrawRepository
import javax.inject.Inject

class GetDrawRestaurantUseCase @Inject constructor(
    private val drawRepository: DrawRepository) {
    suspend operator fun invoke(cuisines: String, situations: String, locations: String):  List<DrawRestaurantData> {
        return drawRepository.getDrawRestaurantData(cuisines, situations, locations)
    }
}