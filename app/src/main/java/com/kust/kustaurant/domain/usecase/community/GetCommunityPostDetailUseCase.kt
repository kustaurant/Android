package com.kust.kustaurant.domain.usecase.community

import com.kust.kustaurant.domain.model.community.CommunityPost
import com.kust.kustaurant.domain.repository.CommunityRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetCommunityPostDetailUseCase @Inject constructor(
    private val communityRepository: CommunityRepository
) {
    suspend operator fun invoke(postId : Long): CommunityPost {
        return communityRepository.getPostDetail(postId)
    }
}