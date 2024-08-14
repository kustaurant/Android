package com.kust.kustaurant.domain.repository

import com.kust.kustaurant.data.model.DetailDataResponse

interface DetailRepository {
    suspend fun getDetailData(
        restaurantId : Int
    ): DetailDataResponse
}