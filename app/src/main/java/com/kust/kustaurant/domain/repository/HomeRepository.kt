package com.kust.kustaurant.domain.repository

import com.kust.kustaurant.data.model.HomeResponse

interface HomeRepository {
    suspend fun getHome(): HomeResponse
}