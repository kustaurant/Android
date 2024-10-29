package com.kust.kustaurant.presentation.ui.detail
data class ReviewData (
    val grade : Double,
    val userName : String,
    val reviewTime : String,
    val userText : String,
    val reviewLike : Int,
    val reviewHate : Int,
    val replyData : ArrayList<ReviewReplyData>
)

data class ReviewReplyData (
    val replyUserName : String,
    val replyReviewTime : String,
    val replyReviewText : String,
    val replyReviewLike : Int,
    val replyReviewHate : Int
)
