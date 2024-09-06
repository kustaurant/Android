package com.kust.kustaurant.data.repository

import com.kust.kustaurant.data.remote.CommunityApi
import com.kust.kustaurant.domain.model.CommunityPost
import com.kust.kustaurant.domain.model.CommunityRanking
import com.kust.kustaurant.domain.repository.CommunityRepository
import javax.inject.Inject

class CommunityRepositoryImpl @Inject constructor(
    private val communityApi : CommunityApi
) : CommunityRepository {
    override suspend fun getCommunityPostListData(postCategory: String, page: Int, sort: String): List<CommunityPost> {
        return communityApi.getCommunityPostListData(postCategory, page, sort)
    }

    override suspend fun getCommunityRankingListData(
        sort: String
    ): List<CommunityRanking> {
        return communityApi.getCommunityRankingListData(sort)
    }

    override suspend fun getCommunityPostDetailData(postId: Int): CommunityPost {
        return communityApi.getCommunityPostDetailData(postId)
    }
}
