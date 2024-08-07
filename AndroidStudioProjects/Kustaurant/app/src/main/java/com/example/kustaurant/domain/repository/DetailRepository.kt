package com.example.kustaurant.domain.repository

import com.example.kustaurant.data.model.DetailDataResponse

interface DetailRepository {
    suspend fun getDetailData(
        restaurantId : Int
    ): DetailDataResponse
}