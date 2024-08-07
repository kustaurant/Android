package com.example.kustaurant.data.repository


import com.example.kustaurant.data.model.DetailDataResponse
import com.example.kustaurant.data.remote.DetailApi
import com.example.kustaurant.domain.repository.DetailRepository
import javax.inject.Inject

class DetailRepositoryImpl @Inject constructor(
    private val detailApi: DetailApi
) : DetailRepository {
    override suspend fun getDetailData(restaurantId: Int) : DetailDataResponse{
        return detailApi.getDetailData(restaurantId)
    }
}