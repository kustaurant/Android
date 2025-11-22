package com.kust.kustaurant.domain.usecase.detail

import com.kust.kustaurant.data.model.CommentReplyResponse
import com.kust.kustaurant.domain.repository.DetailRepository
import javax.inject.Inject

class PostCommentReplyDataUseCase @Inject constructor(
    private val detailRepository: DetailRepository
) {
    suspend operator fun invoke(restaurantId: Int, evalCommentId: Int, body: String): CommentReplyResponse {
        return detailRepository.postCommentReplyData(restaurantId, evalCommentId, body)
    }
}

