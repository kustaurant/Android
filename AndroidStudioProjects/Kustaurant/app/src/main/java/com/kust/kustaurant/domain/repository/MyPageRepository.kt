package com.kust.kustaurant.domain.repository

import com.kust.kustaurant.data.model.MyCommentResponse
import com.kust.kustaurant.data.model.MyCommunityListResponse
import com.kust.kustaurant.data.model.MyEvaluateResponse
import com.kust.kustaurant.data.model.MyFavoriteResponse
import com.kust.kustaurant.data.model.MyPageResponse
import com.kust.kustaurant.data.model.MyScrapResponse

interface MyPageRepository {
    suspend fun getFavoriteData(
    ): List<MyFavoriteResponse>

    suspend fun getEvaluateData(
    ): List<MyEvaluateResponse>

    suspend fun getCommentScrapData(
    ): List<MyScrapResponse>

    suspend fun getCommunityListData(
    ): List<MyCommunityListResponse>

    suspend fun getCommunityCommentData(
    ): List<MyCommentResponse>

    suspend fun getMyPageData(
    ): List<MyPageResponse>
}