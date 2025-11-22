package com.kust.kustaurant.data.model

data class CommentReplyResponse(
    val commentId: Int,
    val writerIconImgUrl: String,
    val writerNickname: String,
    val timeAgo: String,
    val commentBody: String,
    val reactionType: String,
    val commentLikeCount: Int,
    val commentDislikeCount: Int,
    val isCommentMine: Boolean
)

