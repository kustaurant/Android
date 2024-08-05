package com.example.kustaurant.domain.repository

interface DetailRepository {
    suspend fun getDetailData(
        restaurantId : Int,



    )
}