package com.kust.kustaurant.data.remote

import com.kust.kustaurant.domain.model.CommunityPost
import com.kust.kustaurant.domain.model.CommunityRanking
import retrofit2.http.GET
import retrofit2.http.Query

interface CommunityApi {
    @GET("/api/v1/community/posts")
    suspend fun getCommunityPostListData(
        @Query("postCategory") postCategory : String,
        @Query("page") page : Int,
        @Query("sort") sort : String
    ) : List<CommunityPost>

    @GET("/api/v1/community/ranking")
    suspend fun getCommunityRankingListData(
        @Query("sort") sort : String,
    ) : List<CommunityRanking>
}