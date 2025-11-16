package com.kust.kustaurant.domain.usecase.detail

import com.kust.kustaurant.data.model.FavoriteResponse
import com.kust.kustaurant.domain.repository.DetailRepository
import javax.inject.Inject

class DeleteFavoriteUseCase @Inject constructor(
    private val detailRepository: DetailRepository
) {
    suspend operator fun invoke(restaurantId: Int): FavoriteResponse {
        return detailRepository.deleteFavorite(restaurantId)
    }
}

