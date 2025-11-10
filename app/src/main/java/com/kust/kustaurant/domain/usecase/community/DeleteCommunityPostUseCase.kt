package com.kust.kustaurant.domain.usecase.community

import com.kust.kustaurant.domain.repository.CommunityRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteCommunityPostUseCase @Inject constructor(
    private val communityRepository: CommunityRepository
){
    suspend operator fun invoke(postId : Long) {
        communityRepository.deletePost(postId)
    }
}