package com.kust.kustaurant.domain.usecase.community

import com.kust.kustaurant.data.model.community.CommunityCommentReactionResponse
import com.kust.kustaurant.domain.model.community.LikeEvent
import com.kust.kustaurant.domain.repository.CommunityRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostCommunityPostCommentReactUseCase @Inject constructor(
    private val communityRepository: CommunityRepository
){
    suspend operator fun invoke(commentId : Long, reaction : LikeEvent?) : CommunityCommentReactionResponse {
        return communityRepository.postCommentLikeToggle(commentId, reaction)
    }
}