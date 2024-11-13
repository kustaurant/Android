package com.kust.kustaurant.data.repository

import com.kust.kustaurant.data.model.CommunityPostCommentReactResponse
import com.kust.kustaurant.data.model.CommunityPostLikeResponse
import com.kust.kustaurant.data.model.CommunityPostScrapResponse
import com.kust.kustaurant.data.model.CommunityPostUploadImageResponse
import com.kust.kustaurant.data.remote.CommunityApi
import com.kust.kustaurant.domain.model.CommunityPost
import com.kust.kustaurant.domain.model.CommunityPostComment
import com.kust.kustaurant.domain.model.CommunityRanking
import com.kust.kustaurant.domain.repository.CommunityRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class CommunityRepositoryImpl @Inject constructor(
    private val communityApi : CommunityApi
) : CommunityRepository {
    override suspend fun getPostListData(postCategory: String, page: Int, sort: String): List<CommunityPost> {
        return communityApi.getCommunityPostListData(postCategory, page, sort)
    }

    override suspend fun getRankingListData(
        sort: String
    ): List<CommunityRanking> {
        return communityApi.getCommunityRankingListData(sort)
    }

    override suspend fun getPostDetailData(postId: Int): CommunityPost {
        return communityApi.getCommunityPostDetailData(postId)
    }

    override suspend fun postPostCreate(
        title: String,
        postCategory: String,
        content: String,
    ): CommunityPost {
        return communityApi.postPostCreate(title, postCategory, content, null )
    }

    override suspend fun postUploadImage(image: MultipartBody.Part): CommunityPostUploadImageResponse {
        return communityApi.postCommunityUploadImage(image)
    }

    override suspend fun postPostDetailScrap(postId: Int): CommunityPostScrapResponse {
        return communityApi.postCommunityPostDetailScrap(postId)
    }

    override suspend fun postPostDetailLike(postId: Int): CommunityPostLikeResponse {
        return communityApi.postCommunityPostDetailLike(postId)
    }

    override suspend fun patchPostModify(
        postId: String,
        title: String,
        postCategory: String,
        content: String
    ) {
        return communityApi.patchPostModify(postId, title, postCategory, content, null )
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
