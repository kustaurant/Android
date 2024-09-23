package com.kust.kustaurant.domain.usecase.detail

import com.kust.kustaurant.data.model.CommentDataResponse
import com.kust.kustaurant.data.model.DetailDataResponse
import com.kust.kustaurant.data.model.EvaluationDataRequest
import com.kust.kustaurant.domain.repository.DetailRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class PostEvaluationDataUseCase @Inject constructor(
    private val detailRepository: DetailRepository
) {
    suspend operator fun invoke(
        restaurantId: Int,
        evaluationScore: RequestBody,
        evaluationSituations: List<MultipartBody.Part>,
        evaluationComment: RequestBody?,
        newImage: MultipartBody.Part?
    ): List<CommentDataResponse> {
        return detailRepository.postEvaluationData(
            restaurantId,
            evaluationScore,
            evaluationSituations,
            evaluationComment,
            newImage
        )
    }
}
