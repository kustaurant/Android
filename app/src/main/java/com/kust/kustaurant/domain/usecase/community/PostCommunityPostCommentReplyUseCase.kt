package com.kust.kustaurant.domain.usecase.community

import com.kust.kustaurant.domain.model.CommunityPostComment
import com.kust.kustaurant.domain.repository.CommunityRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostCommunityPostCommentReplyUseCase @Inject constructor(
    private val communityRepository: CommunityRepository
){
    suspend operator fun invoke(content : String, postId : String, parentCommentId : String) : List<CommunityPostComment> {
        return communityRepository.postCommunityPostCommentReply(content, postId, parentCommentId)
    }
}