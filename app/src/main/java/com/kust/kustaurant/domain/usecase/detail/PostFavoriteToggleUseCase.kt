package com.kust.kustaurant.domain.usecase.detail

import com.kust.kustaurant.data.model.CommentDataResponse
import com.kust.kustaurant.domain.repository.DetailRepository
import javax.inject.Inject

class PostFavoriteToggleUseCase @Inject constructor(
    private val detailRepository: DetailRepository
) {
    suspend operator fun invoke(restaurant: Int): Boolean {
        return detailRepository.postFavoriteToggle(restaurant)
    }
}
