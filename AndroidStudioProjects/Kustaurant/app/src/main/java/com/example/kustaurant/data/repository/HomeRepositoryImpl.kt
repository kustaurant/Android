package com.example.kustaurant.data.repository

import com.example.kustaurant.data.model.HomeResponse
import com.example.kustaurant.data.remote.HomeApi
import com.example.kustaurant.domain.repository.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val homeApi: HomeApi
):HomeRepository{
    override suspend fun getHome(): HomeResponse {
        return homeApi.getHome()
    }
}