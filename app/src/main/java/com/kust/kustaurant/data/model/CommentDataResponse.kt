package com.kust.kustaurant.data.model

data class CommentDataResponse(
    val evalId: Int,
    val evalScore: Double,
    val writerIconImgUrl: String,
    val writerNickname: String,
    val timeAgo: String,
    val evalImgUrl: String?,
    val evalBody: String,
    val reactionType: String?,
    val evalLikeCount: Int,
    val evalDislikeCount: Int,
    val isEvaluationMine: Boolean,
    val evalCommentList: List<ReplyDataResponse>?
)

data class ReplyDataResponse(
    val commentId: Int,
    val writerIconImgUrl: String,
    val writerNickname: String,
    val timeAgo: String,
    val commentBody: String,
    val reactionType: String?,
    val commentLikeCount: Int,
    val commentDislikeCount: Int,
    val isCommentMine: Boolean
)
