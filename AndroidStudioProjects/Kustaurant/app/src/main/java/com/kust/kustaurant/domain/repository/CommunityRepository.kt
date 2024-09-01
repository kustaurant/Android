package com.kust.kustaurant.domain.repository

import com.kust.kustaurant.domain.model.CommunityPost
import com.kust.kustaurant.domain.model.CommunityRanking

interface CommunityRepository {
    suspend fun getCommunityPostListData(
        postCategory: String,
        page: Int,
        sort: String
    ): List<CommunityPost>

    suspend fun getCommunityRankingListData(
        sort: String
    ): List<CommunityRanking>
}
