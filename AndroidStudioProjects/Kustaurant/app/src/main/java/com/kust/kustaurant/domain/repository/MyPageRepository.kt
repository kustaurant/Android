package com.kust.kustaurant.domain.repository

import com.kust.kustaurant.data.model.MyCommentResponse
import com.kust.kustaurant.data.model.MyCommunityListResponse
import com.kust.kustaurant.data.model.MyEvaluateResponse
import com.kust.kustaurant.data.model.MyFavoriteResponse
import com.kust.kustaurant.data.model.MyPageResponse
import com.kust.kustaurant.data.model.MyProfileRequest
import com.kust.kustaurant.data.model.MyProfileResponse
import com.kust.kustaurant.data.model.MyScrapResponse

interface MyPageRepository {
    suspend fun getProfileData(
    ): MyProfileResponse

    suspend fun patchProfileData(
        nickname : String,
        email : String,
        phoneNumber : String
    )

    suspend fun postFeedBackData(
        feedback : String
    )

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
    ): MyPageResponse
}