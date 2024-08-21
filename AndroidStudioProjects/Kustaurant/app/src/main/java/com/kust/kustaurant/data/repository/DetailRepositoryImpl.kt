package com.kust.kustaurant.data.repository


import com.kust.kustaurant.data.model.CommentDataResponse
import com.kust.kustaurant.data.model.DetailDataResponse
import com.kust.kustaurant.data.remote.DetailApi
import com.kust.kustaurant.domain.repository.DetailRepository
import com.kust.kustaurant.presentation.ui.mypage.CommentData
import javax.inject.Inject

class DetailRepositoryImpl @Inject constructor(
    private val detailApi: DetailApi
) : DetailRepository {
    override suspend fun getDetailData(restaurantId: Int) : DetailDataResponse{
        return detailApi.getDetailData(restaurantId)
    }

    override suspend fun getCommentData(restaurantId: Int, sort: String) : List<CommentDataResponse>{
        return detailApi.getCommentData(restaurantId, sort)
    }

    override suspend fun postCommentData(restaurantId: Int, commentId : Int, inputText : String) : List<CommentDataResponse>{
        return detailApi.postCommentData(restaurantId, commentId, inputText)
    }
}