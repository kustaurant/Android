package com.kust.kustaurant.domain.usecase.detail

import com.kust.kustaurant.data.model.EvalCommentReactionResponse
import com.kust.kustaurant.domain.repository.DetailRepository
import javax.inject.Inject

class PutEvalCommentReactionUseCase @Inject constructor(
    private val detailRepository: DetailRepository
) {
    suspend operator fun invoke(evalCommentId: Int, reaction: String?): EvalCommentReactionResponse {
        return detailRepository.putEvalCommentReaction(evalCommentId, reaction)
    }
}

