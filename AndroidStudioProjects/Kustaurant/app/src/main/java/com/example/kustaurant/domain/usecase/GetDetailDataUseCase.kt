package com.example.kustaurant.domain.usecase

import com.example.kustaurant.data.model.DetailDataResponse
import com.example.kustaurant.domain.repository.DetailRepository
import javax.inject.Inject

class GetDetailDataUseCase @Inject constructor(
    private val detailRepository: DetailRepository
){
    suspend operator fun invoke(restaurant: Int) : DetailDataResponse{
        return detailRepository.getDetailData(restaurant)
    }
}