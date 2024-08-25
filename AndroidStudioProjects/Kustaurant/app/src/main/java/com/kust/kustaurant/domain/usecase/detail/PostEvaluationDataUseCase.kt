package com.kust.kustaurant.domain.usecase.detail

import com.kust.kustaurant.data.model.CommentDataResponse
import com.kust.kustaurant.data.model.DetailDataResponse
import com.kust.kustaurant.data.model.EvaluationDataRequest
import com.kust.kustaurant.domain.repository.DetailRepository
import javax.inject.Inject

class PostEvaluationDataUseCase @Inject constructor(
    private val detailRepository: DetailRepository
) {
    suspend operator fun invoke(restaurant: Int, request: EvaluationDataRequest): DetailDataResponse {
        return detailRepository.postEvaluationData(restaurant, request)
    }
}
