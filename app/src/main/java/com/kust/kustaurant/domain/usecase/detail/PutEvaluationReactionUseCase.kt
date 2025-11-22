package com.kust.kustaurant.domain.usecase.detail

import com.kust.kustaurant.data.model.EvaluationReactionResponse
import com.kust.kustaurant.domain.repository.DetailRepository
import javax.inject.Inject

class PutEvaluationReactionUseCase @Inject constructor(
    private val detailRepository: DetailRepository
) {
    suspend operator fun invoke(
        evaluationId: Int,
        reaction: String?,
        userId: Int,
        role: String
    ): EvaluationReactionResponse {
        return detailRepository.putEvaluationReaction(evaluationId, reaction, userId, role)
    }
}

