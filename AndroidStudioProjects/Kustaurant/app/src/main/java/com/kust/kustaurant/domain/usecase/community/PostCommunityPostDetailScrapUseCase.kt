package com.kust.kustaurant.domain.usecase.community

import com.kust.kustaurant.data.model.CommunityPostScrapResponse
import com.kust.kustaurant.domain.repository.CommunityRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostCommunityPostDetailScrapUseCase @Inject constructor(
    private val communityRepository: CommunityRepository
) {
    suspend operator fun invoke(postId: Int): CommunityPostScrapResponse {
        return communityRepository.postCommunityPostDetailScrap(postId)
    }
}