package com.kust.kustaurant.data.remote

import com.kust.kustaurant.data.model.MyCommentResponse
import com.kust.kustaurant.data.model.MyCommunityListResponse
import com.kust.kustaurant.data.model.MyEvaluateResponse
import com.kust.kustaurant.data.model.MyFavoriteResponse
import com.kust.kustaurant.data.model.MyPageResponse
import com.kust.kustaurant.data.model.MyProfileRequest
import com.kust.kustaurant.data.model.MyProfileResponse
import com.kust.kustaurant.data.model.MyScrapResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST

interface MyPageApi {
    // 피드백 Body 질문
    @POST("/api/v1/auth/mypage/feedback")
    suspend fun postFeedBackData(
        @Body feedBack : String
    )
    @GET("/api/v1/auth/mypage/profile")
    suspend fun getProfileData(
    ) : MyProfileResponse

    // 사진 미구현
    @PATCH("/api/v1/auth/mypage/profile")
    suspend fun patchProfileData(
        @Body request : MyProfileRequest
    )

    @GET("/api/v1/auth/mypage/favorite-restuarnt-list")
    suspend fun getFavoriteData(
    ) : List<MyFavoriteResponse>

    @GET("/api/v1/auth/mypage/evaluate-restuarnt-list")
    suspend fun getEvaluateData(
    ) : List<MyEvaluateResponse>

    @GET("/api/v1/auth/mypage/community-scrap-list")
    suspend fun getCommunityScrapData(
    ) : List<MyScrapResponse>

    @GET("/api/v1/auth/mypage/community-list")
    suspend fun getMyCommunityData(
    ) : List<MyCommunityListResponse>

    @GET("/api/v1/auth/mypage/community-comment-list")
    suspend fun getMyCommunityCommentData(
    ) : List<MyCommentResponse>

    @GET("/api/v1/mypage")
    suspend fun getMyPageData(
    ) : MyPageResponse
}