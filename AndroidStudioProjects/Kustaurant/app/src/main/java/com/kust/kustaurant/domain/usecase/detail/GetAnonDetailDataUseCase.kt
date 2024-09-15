package com.kust.kustaurant.domain.usecase.detail

import com.kust.kustaurant.data.model.DetailDataResponse
import com.kust.kustaurant.domain.repository.DetailRepository
import javax.inject.Inject

class GetAnonDetailDataUseCase @Inject constructor(
    private val detailRepository: DetailRepository
){
    suspend operator fun invoke(restaurant: Int) : DetailDataResponse {
        return detailRepository.getAnonDetailData(restaurant)
    }
}