package com.example.kustaurant.domain.repository

import com.example.kustaurant.data.model.DrawRestaurantData


interface DrawRepository {
    suspend fun getDrawRestaurantData(cuisines: String, situations : String, locations: String):  List<DrawRestaurantData>
}
