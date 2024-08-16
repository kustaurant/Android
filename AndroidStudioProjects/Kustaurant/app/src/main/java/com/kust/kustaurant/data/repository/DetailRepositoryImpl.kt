package com.kust.kustaurant.data.repository


import com.kust.kustaurant.data.model.DetailDataResponse
import com.kust.kustaurant.data.remote.DetailApi
import com.kust.kustaurant.domain.repository.DetailRepository
import javax.inject.Inject

class DetailRepositoryImpl @Inject constructor(
    private val detailApi: DetailApi
) : DetailRepository {
    override suspend fun getDetailData(restaurantId: Int) : DetailDataResponse{
        return detailApi.getDetailData(restaurantId)
    }
}