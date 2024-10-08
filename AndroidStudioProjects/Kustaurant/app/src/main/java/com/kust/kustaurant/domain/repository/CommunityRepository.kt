package com.kust.kustaurant.domain.repository

import com.kust.kustaurant.data.model.CommunityPostCommentReactResponse
import com.kust.kustaurant.data.model.CommunityPostLikeResponse
import com.kust.kustaurant.data.model.CommunityPostScrapResponse
import com.kust.kustaurant.domain.model.CommunityPost
import com.kust.kustaurant.domain.model.CommunityPostComment
import com.kust.kustaurant.domain.model.CommunityRanking

interface CommunityRepository {
    suspend fun getCommunityPostListData(
        postCategory: String,
        page: Int,
        sort: String
    ): List<CommunityPost>

    suspend fun getCommunityRankingListData(
        sort: String
    ): List<CommunityRanking>

    suspend fun getCommunityPostDetailData(
        postId: Int,
    ): CommunityPost

    suspend fun postCommunityPostDetailScrap(
        postId : Int,
    ) : CommunityPostScrapResponse

    suspend fun postCommunityPostDetailLike(
        postId : Int,
    ) : CommunityPostLikeResponse

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


