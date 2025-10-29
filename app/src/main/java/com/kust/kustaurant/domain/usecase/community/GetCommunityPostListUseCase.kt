package com.kust.kustaurant.domain.usecase.community

import com.kust.kustaurant.domain.model.community.CategorySort
import com.kust.kustaurant.domain.model.community.CommunityPostListItem
import com.kust.kustaurant.domain.model.community.PostCategory
import com.kust.kustaurant.domain.repository.CommunityRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetCommunityPostListUseCase @Inject constructor(
    private val communityRepository: CommunityRepository
) {
    suspend operator fun invoke(postCategory: PostCategory, page: Int, sort: CategorySort): List<CommunityPostListItem> {
        return communityRepository.getPostList(postCategory, page, sort)
    }
}