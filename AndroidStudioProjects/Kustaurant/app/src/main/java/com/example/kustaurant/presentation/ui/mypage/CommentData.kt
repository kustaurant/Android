package com.example.kustaurant.presentation.ui.mypage

data class CommentData (
    val postCategory: String,
    val postTitle: String,
    val postCommentBody: String,
    val commentLikeCount: Int
)