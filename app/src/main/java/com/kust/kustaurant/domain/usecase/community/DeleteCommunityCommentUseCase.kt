package com.kust.kustaurant.domain.usecase.community

import com.kust.kustaurant.domain.repository.CommunityRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteCommunityCommentUseCase @Inject constructor(
    private val communityRepository: CommunityRepository
){
    suspend operator fun invoke(commentId : Int) {
        return communityRepository.deletePostComment(commentId)
    }
}