package com.kust.kustaurant.domain.model

data class CommunityPost(
    val postId: Int,
    val postTitle: String,
    val postBody: String,
    val status: String,
    val postCategory: String,
    val createdAt: String,
    val updatedAt: String?,
    val likeCount: Int,
    val user: User,
    val postCommentList: List<CommunityPostComment>,
    val timeAgo : String,
    val commentCount : Int,
)

data class User(
    val userNickname: String,
    val rankImg: String,
    val evaluationCount: Int,
    val rank: String?
)

data class CommunityPostComment(
    val commentId: Int,
    val commentBody: String,
    val user: User,
    val likeCount: Int,
    val dislikeCount: Int
)
