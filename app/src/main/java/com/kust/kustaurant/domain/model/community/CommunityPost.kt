package com.kust.kustaurant.domain.model.community

data class CommunityPost(
    val postId: Long,
    val category: String,
    val title: String,
    val body: String,
    val photoUrls: List<String>?,

    val writeruserId: Long,
    val writernickname: String,
    val writerevalCount: Long,
    val writericonUrl: String?,

    val timeAgo: String?,
    val createdAt: String,
    val updatedAt: String?,

    val likeOnlyCount: Long,
    val dislikeOnlyCount: Long,
    val totalLikes: Long,
    val commentCount: Long,
    val scrapCount: Long,
    val visitCount: Long,

    val myReaction: String?,
    val isScrapped: Boolean,
    val isPostMine: Boolean,

    val comments: List<CommunityPostComment>?
)

data class CommunityPostComment(
    val commentId: Long,
    val parentCommentId: Long? = null,
    val body: String,
    val status: String,
    val likeCount: Int,
    val dislikeCount: Int,
    val replies: List<CommunityPostComment> = emptyList(),
    val timeAgo: String,
    val reactionType: String?,
    val isCommentMine: Boolean,

    val writeruserId: Long?,
    val writernickname: String?,
    val writerevalCount: Long?,
    val writericonUrl: String?
)
