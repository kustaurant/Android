package com.kust.kustaurant.domain.usecase.community

import com.kust.kustaurant.domain.model.community.PostCategory
import com.kust.kustaurant.domain.repository.CommunityRepository
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PatchPostModifyUseCase @Inject constructor(
    private val communityRepository: CommunityRepository
){
    suspend operator fun invoke(postId: String, title : String, postCategory : PostCategory, content : String,) {
        return communityRepository.patchPostModify(postId, title, postCategory, content)
    }
}