package com.kust.kustaurant.domain.usecase.community

import com.kust.kustaurant.domain.model.CommunityPost
import com.kust.kustaurant.domain.repository.CommunityRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetCommunityPostListUseCase @Inject constructor(
    private val communityRepository: CommunityRepository
) {
    suspend operator fun invoke(postCategory: String, page: Int, sort: String): List<CommunityPost> {
        return communityRepository.getPostListData(postCategory, page, sort)
    }
}