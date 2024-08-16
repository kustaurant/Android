package com.kust.kustaurant.domain.repository

import com.kust.kustaurant.domain.model.CommunityPost

interface CommunityRepository {
    suspend fun getRestaurantList( ): List<CommunityPost>
}
