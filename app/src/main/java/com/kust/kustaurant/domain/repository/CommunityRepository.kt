package com.kust.kustaurant.domain.repository

import com.kust.kustaurant.data.model.CommunityPostCommentReactResponse
import com.kust.kustaurant.data.model.CommunityPostLikeResponse
import com.kust.kustaurant.data.model.CommunityPostScrapResponse
import com.kust.kustaurant.data.model.CommunityPostUploadImageResponse
import com.kust.kustaurant.domain.model.CommunityPost
import com.kust.kustaurant.domain.model.CommunityPostComment
import com.kust.kustaurant.domain.model.CommunityRanking
import okhttp3.MultipartBody

interface CommunityRepository {
    suspend fun getPostListData(
        postCategory: String,
        page: Int,
        sort: String
    ): List<CommunityPost>

    suspend fun getRankingListData(
        sort: String
    ): List<CommunityRanking>

    suspend fun getPostDetailData(
        postId: Int,
    ): CommunityPost

    suspend fun postPostCreate(
        title : String,
        postCategory : String,
        content : String,
    ) : CommunityPost

    suspend fun postUploadImage(
        image : MultipartBody.Part
    ) : CommunityPostUploadImageResponse

    suspend fun postPostDetailScrap(
        postId : Int,
    ) : CommunityPostScrapResponse

    suspend fun postPostDetailLike(
        postId : Int,
    ) : CommunityPostLikeResponse

    suspend fun patchPostModify(
        postId: String,
        title : String,
        postCategory : String,
        content : String,
    )

    suspend fun postCommunityPostCommentReply(
        content : String,
        postId : String,
        parentCommentId : String,
    ) : CommunityPostComment

    suspend fun postCommentReact(
        commentId : Int,
        action : String,
    ) : CommunityPostCommentReactResponse

    suspend fun deletePost(
        postId : Int,
    )

    suspend fun deletePostComment(
        commentId : Int,
    )
}


