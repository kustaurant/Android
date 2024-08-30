package com.kust.kustaurant.domain.usecase.detail

import com.kust.kustaurant.data.model.DetailDataResponse
import com.kust.kustaurant.data.model.EvaluationDataResponse
import com.kust.kustaurant.domain.repository.DetailRepository
import javax.inject.Inject

class GetEvaluationDataUseCase @Inject constructor(
    private val detailRepository: DetailRepository
){
    suspend operator fun invoke(restaurant: Int) : EvaluationDataResponse {
        return detailRepository.getEvaluationData(restaurant)
    }
}