package com.kust.kustaurant.domain.usecase.community

import com.kust.kustaurant.domain.model.community.CommunityPostComment
import com.kust.kustaurant.domain.repository.CommunityRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostCommunityPostCommentReplyUseCase @Inject constructor(
    private val communityRepository: CommunityRepository
){
    suspend operator fun invoke(content : String, postId : Long, parentCommentId : Long?) : CommunityPostComment {
        return communityRepository.postCommunityCommentReply( postId, content, parentCommentId)
    }
}