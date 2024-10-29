package com.kust.kustaurant.domain.repository

import com.kust.kustaurant.data.model.DrawRestaurantData


interface DrawRepository {
    suspend fun getDrawRestaurantData(cuisines: String, locations: String):  List<DrawRestaurantData>
}
