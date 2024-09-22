package com.kust.kustaurant.domain.usecase.community

import com.kust.kustaurant.domain.repository.CommunityRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostCommunityPostDetailScrap @Inject constructor(
    private val communityRepository: CommunityRepository
) {
//    suspend operator fun invoke(sort: String): List<CommunityRanking> {
//        return communityRepository.getCommunityRankingListData(sort)
//    }
}