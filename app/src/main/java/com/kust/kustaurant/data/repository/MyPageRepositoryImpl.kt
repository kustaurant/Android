package com.kust.kustaurant.data.repository

import com.kust.kustaurant.data.model.MyCommentResponse
import com.kust.kustaurant.data.model.MyCommunityListResponse
import com.kust.kustaurant.data.model.MyEvaluateResponse
import com.kust.kustaurant.data.model.MyFavoriteResponse
import com.kust.kustaurant.data.model.MyPageResponse
import com.kust.kustaurant.data.model.FeedbackRequest
import com.kust.kustaurant.data.model.MyProfileRequest
import com.kust.kustaurant.data.model.MyProfileResponse
import com.kust.kustaurant.data.model.MyScrapResponse
import com.kust.kustaurant.data.remote.MyPageApi
import com.kust.kustaurant.domain.repository.MyPageRepository
import javax.inject.Inject

class MyPageRepositoryImpl @Inject constructor(
    private val myPageApi: MyPageApi
) : MyPageRepository {

    override suspend fun getProfileData(): MyProfileResponse {
        return myPageApi.getProfileData()
    }

    override suspend fun patchProfileData(
        nickname:String,
        email:String,
        phoneNumber : String
    ) {
        val request = MyProfileRequest(nickname, email, phoneNumber)
        return myPageApi.patchProfileData(request)
    }

    override suspend fun postFeedBackData(comment: String) {
        val request = FeedbackRequest(comment)
        return myPageApi.postFeedBackData(request)
    }

    override suspend fun getFavoriteData(): List<MyFavoriteResponse> {
        return myPageApi.getFavoriteData()
    }

    override suspend fun getEvaluateData(): List<MyEvaluateResponse> {
        return myPageApi.getEvaluateData()
    }

    override suspend fun getCommentScrapData(): List<MyScrapResponse> {
        return myPageApi.getCommunityScrapData()
    }

    override suspend fun getCommunityListData(): List<MyCommunityListResponse> {
        return myPageApi.getMyCommunityData()
    }

    override suspend fun getCommunityCommentData(): List<MyCommentResponse> {
        return myPageApi.getMyCommunityCommentData()
    }

    override suspend fun getMyPageData(): MyPageResponse {
        return myPageApi.getMyPageData()
    }
}