package com.kust.kustaurant.domain.usecase.community

import com.kust.kustaurant.data.model.CommunityPostLikeResponse
import com.kust.kustaurant.domain.repository.CommunityRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostPostScrapLikeUseCase @Inject constructor(
    private val communityRepository: CommunityRepository
){
    suspend operator fun invoke(postId : Int) : CommunityPostLikeResponse {
        return communityRepository.postCommunityPostDetailLike(postId)
    }
}
