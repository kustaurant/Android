package com.example.kustaurant.domain.repository

import com.example.kustaurant.data.model.HomeResponse

interface HomeRepository {
    suspend fun getHome(): HomeResponse
}