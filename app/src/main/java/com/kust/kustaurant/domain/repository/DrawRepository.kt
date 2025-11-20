package com.kust.kustaurant.domain.repository

import com.kust.kustaurant.data.model.DrawRestaurant

interface DrawRepository {
    suspend fun getDrawRestaurantData(cuisines: String, locations: String):  List<DrawRestaurant>
}
