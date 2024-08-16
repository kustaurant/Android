package com.kust.kustaurant.data.repository

import com.kust.kustaurant.data.remote.CommunityApi
import com.kust.kustaurant.domain.model.CommunityPost
import com.kust.kustaurant.domain.repository.CommunityRepository
import javax.inject.Inject

class CommunityRepositoryImpl @Inject constructor(
    private val communityApi : CommunityApi
) : CommunityRepository {
    override suspend fun getRestaurantList( ): List<CommunityPost> {
        return communityApi.getCommunityBoardListData()
    }
}