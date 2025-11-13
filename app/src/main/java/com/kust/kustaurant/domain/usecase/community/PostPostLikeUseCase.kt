package com.kust.kustaurant.domain.usecase.community

import com.kust.kustaurant.data.model.community.CommunityPostLikeResponse
import com.kust.kustaurant.domain.model.community.LikeEvent
import com.kust.kustaurant.domain.repository.CommunityRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostPostLikeUseCase @Inject constructor(
    private val communityRepository: CommunityRepository
){
    suspend operator fun invoke(postId : Long, likeEvent : LikeEvent?) : CommunityPostLikeResponse {
        return communityRepository.postPostLikeToggle(postId, likeEvent)
    }
}
