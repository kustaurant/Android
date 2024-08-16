package com.kust.kustaurant.data.remote

import com.kust.kustaurant.domain.model.CommunityPost
import retrofit2.http.GET

interface CommunityApi {
    @GET("/api/v1/community ")
    suspend fun getCommunityBoardListData() : List<CommunityPost>
}