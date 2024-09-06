package com.kust.kustaurant.domain.usecase.community

import com.kust.kustaurant.domain.model.CommunityPost
import com.kust.kustaurant.domain.repository.CommunityRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetCommunityPostDetailUseCase @Inject constructor(
    private val communityRepository: CommunityRepository
) {
    suspend operator fun invoke(postId : Int): CommunityPost {
        return communityRepository.getCommunityPostDetailData(postId)
    }
}