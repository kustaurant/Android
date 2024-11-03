package com.kust.kustaurant.data.repository

import com.kust.kustaurant.data.model.CommunityPostCommentReactResponse
import com.kust.kustaurant.data.model.CommunityPostLikeResponse
import com.kust.kustaurant.data.model.CommunityPostScrapResponse
import com.kust.kustaurant.data.remote.CommunityApi
import com.kust.kustaurant.domain.model.CommunityPost
import com.kust.kustaurant.domain.model.CommunityPostComment
import com.kust.kustaurant.domain.model.CommunityRanking
import com.kust.kustaurant.domain.repository.CommunityRepository
import javax.inject.Inject

class CommunityRepositoryImpl @Inject constructor(
    private val communityApi : CommunityApi
) : CommunityRepository {
    override suspend fun getCommunityPostListData(postCategory: String, page: Int, sort: String): List<CommunityPost> {
        return communityApi.getCommunityPostListData(postCategory, page, sort)
    }

    override suspend fun getCommunityRankingListData(
        sort: String
    ): List<CommunityRanking> {
        return communityApi.getCommunityRankingListData(sort)
    }

    override suspend fun getCommunityPostDetailData(postId: Int): CommunityPost {
        return communityApi.getCommunityPostDetailData(postId)
    }

    override suspend fun postCommunityPostCreate(
        title: String,
        postCategory: String,
        content: String,
        imageFile: String
    ): CommunityPostCommentReactResponse {
        TODO("Not yet implemented")
    }

    override suspend fun postCommunityPostDetailScrap(postId: Int): CommunityPostScrapResponse {
        return communityApi.postCommunityPostDetailScrap(postId)
    }

    override suspend fun postCommunityPostDetailLike(postId: Int): CommunityPostLikeResponse {
        return communityApi.postCommunityPostDetailLike(postId)
    }

    override suspend fun postCommunityPostCommentReply(
        content: String,
        postId: String,
        parentCommentId: String
    ): CommunityPostComment {
        return communityApi.postCommunityPostCommentReply(content, postId, parentCommentId)
    }

    override suspend fun postCommentReact(
        commentId: Int,
        action: String
    ): CommunityPostCommentReactResponse {
        return communityApi.postCommentReact(commentId, action)
    }

    override suspend fun deletePost(postId: Int) {
        return communityApi.deletePost(postId)
    }

    override suspend fun deletePostComment(commentId: Int) {
        return communityApi.deletePostComment(commentId)
    }
}
