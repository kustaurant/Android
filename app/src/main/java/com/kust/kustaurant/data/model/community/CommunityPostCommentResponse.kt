package com.kust.kustaurant.data.model.community

import com.kust.kustaurant.domain.model.community.CommunityPostComment

data class CommunityPostCommentResponse(
    val commentId: Long,
    val parentCommentId: Long?,
    val body: String,
    val status: String,
    val likeCount: Int,
    val dislikeCount: Int,
    val replies: List<CommunityPostCommentResponse>?,
    val timeAgo: String,
    val reactionType: String?,
    val isCommentMine: Boolean,

    val writeruserId: Long?,
    val writernickname: String?,
    val writerevalCount: Long?,
    val writericonUrl: String?
)

fun CommunityPostCommentResponse.toDomain(): CommunityPostComment =
    CommunityPostComment(
        commentId = commentId,
        parentCommentId = parentCommentId,
        body = body,
        status = status,
        likeCount = likeCount,
        dislikeCount = dislikeCount,
        replies = replies?.map { it.toDomain() }.orEmpty(),
        timeAgo = timeAgo,
        reactionType = reactionType,
        isCommentMine = isCommentMine,
        writeruserId = writeruserId,
        writernickname = writernickname,
        writerevalCount = writerevalCount,
        writericonUrl = writericonUrl
    )