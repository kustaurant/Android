package com.kust.kustaurant.domain.model.community

data class PostModification (
    val postId: Long,
    val category: String,
    val title: String,
    val body: String,
    val photoUrls: List<String>
)