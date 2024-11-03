package com.kust.kustaurant.domain.usecase.community

import com.kust.kustaurant.data.model.CommunityPostCommentReactResponse
import com.kust.kustaurant.domain.repository.CommunityRepository
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PostCommunityPostCreate @Inject constructor(
    private val communityRepository: CommunityRepository
){
    suspend operator fun invoke(title : String,  postCategory : String, content : String,  imageFile : String,) : CommunityPostCommentReactResponse {
        return communityRepository.postCommunityPostCreate(title, postCategory, content, imageFile)
    }
}