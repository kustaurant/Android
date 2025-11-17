package com.kust.kustaurant.domain.usecase.detail

import com.kust.kustaurant.domain.repository.DetailRepository
import javax.inject.Inject

class DeleteCommentDataUseCase @Inject constructor(
    private val detailRepository: DetailRepository
) {
    suspend operator fun invoke(restaurantId: Int, evalCommentId: Int) {
        return detailRepository.deleteCommentData(restaurantId, evalCommentId)
    }
}
