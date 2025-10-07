package com.kust.kustaurant.domain.usecase.community

import com.kust.kustaurant.domain.model.community.CommunityPost
import com.kust.kustaurant.domain.model.community.PostCategory
import com.kust.kustaurant.domain.repository.CommunityRepository
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PostCommunityPostCreateUseCase @Inject constructor(
    private val communityRepository: CommunityRepository
){
    suspend operator fun invoke(title : String, postCategory : PostCategory, content : String) : CommunityPost {
        return communityRepository.postPostCreate(title, postCategory, content)
    }
}