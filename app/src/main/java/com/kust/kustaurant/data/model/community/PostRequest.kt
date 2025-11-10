package com.kust.kustaurant.data.model.community

data class PostRequest(
    val title: String,
    val category: String,
    val content: String
) {
    init {
        require(title.length in 1..100) { "Title must be between 1 and 100 characters." }
        require(content.length in 1..10000) { "Content must be between 1 and 10000 characters." }
    }
}

