package com.kust.kustaurant.domain.usecase.community

import com.kust.kustaurant.domain.model.community.CommunityRanking
import com.kust.kustaurant.domain.model.community.RankingSort
import com.kust.kustaurant.domain.repository.CommunityRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetCommunityRankingListUseCase @Inject constructor(
    private val communityRepository: CommunityRepository
) {
    suspend operator fun invoke(sort: RankingSort): List<CommunityRanking> {
        return communityRepository.getRankingList(sort)
    }
}