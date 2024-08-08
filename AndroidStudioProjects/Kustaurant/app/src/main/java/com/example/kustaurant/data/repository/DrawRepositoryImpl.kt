package com.example.kustaurant.data.repository

import com.example.kustaurant.data.model.DrawRestaurantData
import com.example.kustaurant.data.remote.KustaurantApi
import com.example.kustaurant.domain.repository.DrawRepository
import javax.inject.Inject

class DrawRepositoryImpl @Inject constructor(
    private val kustaurantApi: KustaurantApi
) : DrawRepository {
    override suspend fun getDrawRestaurantData(cuisines: String, situations: String, locations: String): List<DrawRestaurantData> {
        return kustaurantApi.getDrawRestaurantsData(cuisines, situations, locations)
    }
}
