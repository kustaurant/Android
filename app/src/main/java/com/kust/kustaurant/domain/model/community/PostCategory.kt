package com.kust.kustaurant.domain.model.community

enum class PostCategory(val value : String){
        ALL("ALL"),
        FREE("FREE"),
        COLUMN("COLUMN"),
        SUGGESTION("SUGGESTION")
}

fun String.toPostCategory(): PostCategory {
        return when (this) {
                "자유게시판" -> PostCategory.FREE
                "칼럼게시판" -> PostCategory.COLUMN
                "건의게시판" -> PostCategory.SUGGESTION
                else -> PostCategory.ALL
        }
}
