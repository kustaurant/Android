package com.kust.kustaurant.data.repository

import com.kust.kustaurant.data.model.commnity.CommunityCommentReactionResponse
import com.kust.kustaurant.data.model.commnity.CommunityPostLikeResponse
import com.kust.kustaurant.data.model.commnity.CommunityPostScrapResponse
import com.kust.kustaurant.data.model.commnity.CommunityPostUploadImageResponse
import com.kust.kustaurant.data.model.commnity.PostCommentRequest
import com.kust.kustaurant.data.model.commnity.PostRequest
import com.kust.kustaurant.data.model.commnity.toDomain
import com.kust.kustaurant.data.remote.CommunityApi
import com.kust.kustaurant.domain.model.community.AuthUserInfo
import com.kust.kustaurant.domain.model.community.CategorySort
import com.kust.kustaurant.domain.model.community.CommunityPost
import com.kust.kustaurant.domain.model.community.CommunityPostComment
import com.kust.kustaurant.domain.model.community.CommunityPostListItem
import com.kust.kustaurant.domain.model.community.CommunityRanking
import com.kust.kustaurant.domain.model.community.LikeEvent
import com.kust.kustaurant.domain.model.community.PostCategory
import com.kust.kustaurant.domain.model.community.PostModification
import com.kust.kustaurant.domain.model.community.RankingSort
import com.kust.kustaurant.domain.repository.CommunityRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class CommunityRepositoryImpl @Inject constructor(
    private val communityApi : CommunityApi
) : CommunityRepository {
    override suspend fun getPostList(postCategory: PostCategory, page: Int, sort: CategorySort): List<CommunityPostListItem> {
        return communityApi
            .getCommunityPostListData(postCategory.value, page, sort.value)
    }

    override suspend fun getRankingList(
        sort: RankingSort
    ): List<CommunityRanking> {
        return communityApi.getCommunityRankingListData(sort.value)
    }

    override suspend fun getPostDetail(postId: Long): CommunityPost {
        return communityApi.getCommunityPostDetailData(postId).toDomain()
    }

    override suspend fun postPostCreate(
        title: String,
        postCategory: PostCategory,
        content: String,
    ): CommunityPost {
        val request = PostRequest(title, postCategory.value, content)
        return communityApi.postPostCreate(request).toDomain()
    }

    override suspend fun postUploadImage(image: MultipartBody.Part): CommunityPostUploadImageResponse {
        return communityApi.postCommunityUploadImage(image)
    }

    override suspend fun postPostDetailScrap(postId: Long, scrapped : Boolean): CommunityPostScrapResponse {
        return communityApi.postCommunityPostDetailScrap(postId, scrapped)
    }

    override suspend fun postPostLikeToggle(postId: Long, likeEvent : LikeEvent?): CommunityPostLikeResponse {
        return communityApi.postCommunityPostLikeToggle(postId, likeEvent?.value)
    }

    override suspend fun patchPostModify(
        postId: String,
        title: String,
        postCategory: PostCategory,
        content: String
    ) {
        val request = PostRequest(title, postCategory.value, content)
        return communityApi.patchModifyPost(postId, request)
    }

    override suspend fun getPostModify(postId: Long, user: AuthUserInfo): PostModification {
        return communityApi.getModifyPost(postId, user).toDomain()
    }

    override suspend fun postCommunityCommentReply(
        postId : Long,
        content : String,
        parentCommentId : Long?,
    ): CommunityPostComment {
        val request = PostCommentRequest(content, parentCommentId)
        return communityApi.postCommunityPostCommentReply(postId, request).toDomain()
    }

    override suspend fun postCommentLikeToggle(
        commentId: Long,
        reaction: LikeEvent?
    ): CommunityCommentReactionResponse {
        return communityApi.putCommentLikeToggle(commentId, reaction?.value)
    }

    override suspend fun deletePost(postId: Long) {
        return communityApi.deletePost(postId)
    }

    override suspend fun deleteCommunityComment(commentId: Long) {
        return communityApi.deletePostComment(commentId)
    }
}
