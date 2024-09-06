package com.kust.kustaurant.data.model

data class CommentDataResponse(
    val commentId : Int,
    val commentScore: Double,
    val commentIconImgUrl: String,
    val commentNickname: String,
    val commentTime: String,
    val commentImgUrl: String,
    val commentBody: String,
    val commentLikeStatus: Int,
    val commentLikeCount: Int,
    val commentDislikeCount: Int,
    val isCommentMine : Boolean,
    var commentReplies: ArrayList<ReplyDataResponse>
)

data class ReplyDataResponse(
    val commentId : Int,
    val commentScore: Double,
    val commentIconImgUrl: String,
    val commentNickname: String,
    val commentTime: String,
    val commentImgUrl: String,
    val commentBody: String,
    val commentLikeStatus: Int,
    val commentLikeCount: Int,
    val commentDislikeCount: Int,
    val isCommentMine: Boolean,
    val commentReplies: String
)
