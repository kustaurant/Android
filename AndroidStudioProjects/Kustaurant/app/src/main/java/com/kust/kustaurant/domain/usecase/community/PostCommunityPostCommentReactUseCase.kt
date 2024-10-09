package com.kust.kustaurant.domain.usecase.community

import com.kust.kustaurant.data.model.CommunityPostCommentReactResponse
import com.kust.kustaurant.domain.repository.CommunityRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostCommunityPostCommentReactUseCase @Inject constructor(
    private val communityRepository: CommunityRepository
){
    suspend operator fun invoke(commentId : Int, action : String) : CommunityPostCommentReactResponse {
        return communityRepository.postCommentReact(commentId, action)
    }
}