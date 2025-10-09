package com.kust.kustaurant.domain.model.community

enum class PostCategory(val value : String){
        ALL("ALL"),
        FREE("FREE"),
        COLUMN("COLUMN"),
        SUGGESTION("SUGGESTION")
}

fun String.toPostCategory(): PostCategory {
        return when (this) {
                "all" -> PostCategory.ALL
                "free" -> PostCategory.FREE
                "column" -> PostCategory.COLUMN
                "suggestion" -> PostCategory.SUGGESTION
                else -> PostCategory.ALL
        }
}
