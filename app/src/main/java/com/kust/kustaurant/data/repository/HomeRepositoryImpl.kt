package com.kust.kustaurant.data.repository

import com.kust.kustaurant.data.model.HomeResponse
import com.kust.kustaurant.data.remote.HomeApi
import com.kust.kustaurant.domain.repository.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val homeApi: HomeApi
):HomeRepository{
    override suspend fun getHome(): HomeResponse {
        return homeApi.getHome()
    }
}