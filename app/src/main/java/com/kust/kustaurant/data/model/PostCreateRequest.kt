package com.kust.kustaurant.data.model

data class PostCreateRequest(
    val title: String,
    val postCategory: String,
    val content: String,
    val imageFile: String? = null
)
