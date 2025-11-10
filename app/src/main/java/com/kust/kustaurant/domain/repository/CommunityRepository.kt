package com.kust.kustaurant.domain.repository

import com.kust.kustaurant.data.model.community.CommunityCommentReactionResponse
import com.kust.kustaurant.data.model.community.CommunityPostLikeResponse
import com.kust.kustaurant.data.model.community.CommunityPostScrapResponse
import com.kust.kustaurant.data.model.community.CommunityPostUploadImageResponse
import com.kust.kustaurant.domain.model.community.AuthUserInfo
import com.kust.kustaurant.domain.model.community.CategorySort
import com.kust.kustaurant.domain.model.community.CommunityPost
import com.kust.kustaurant.domain.model.community.CommunityPostComment
import com.kust.kustaurant.domain.model.community.CommunityPostListItem
import com.kust.kustaurant.domain.model.community.CommunityRanking
import com.kust.kustaurant.domain.model.community.LikeEvent
import com.kust.kustaurant.domain.model.community.PostModification
import com.kust.kustaurant.domain.model.community.PostCategory
import com.kust.kustaurant.domain.model.community.RankingSort
import okhttp3.MultipartBody

interface CommunityRepository {
    suspend fun getPostList(
        postCategory: PostCategory,
        page: Int,
        sort: CategorySort
    ): List<CommunityPostListItem>

    suspend fun getRankingList(
        sort: RankingSort
    ): List<CommunityRanking>

    suspend fun getPostDetail(
        postId: Long,
    ): CommunityPost

    suspend fun postPostCreate(
        title : String,
        postCategory : PostCategory,
        content : String,
    ) : CommunityPost

    suspend fun postUploadImage(
        image : MultipartBody.Part
    ) : CommunityPostUploadImageResponse

    suspend fun postPostDetailScrap(
        postId : Long,
        scrapped : Boolean
    ) : CommunityPostScrapResponse

    suspend fun postPostLikeToggle(
        postId : Long,
        likeEvent : LikeEvent?,
    ) : CommunityPostLikeResponse

    suspend fun patchPostModify(
        postId: String,
        title : String,
        postCategory : PostCategory,
        content : String,
    )

    suspend fun getPostModify(
        postId: Long,
        user : AuthUserInfo,
    ) : PostModification

    suspend fun postCommunityCommentReply(
        postId : Long,
        content : String,
        parentCommentId : Long?,
    ) : CommunityPostComment

    suspend fun postCommentLikeToggle(
        commentId : Long,
        reaction : LikeEvent?,
    ) : CommunityCommentReactionResponse

    suspend fun deletePost(
        postId : Long,
    )

    suspend fun deleteCommunityComment(
        commentId : Long,
    )
}


