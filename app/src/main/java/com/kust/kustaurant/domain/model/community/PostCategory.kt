package com.kust.kustaurant.domain.model.community

enum class PostCategory(val value: String, val displayName: String) {
        ALL("ALL", "전체게시판"),
        FREE("FREE", "자유게시판"),
        COLUMN("COLUMN", "칼럼게시판"),
        SUGGESTION("SUGGESTION", "건의게시판");
}

fun String.toPostCategory(): PostCategory {
        return when (this) {
                "전체게시판" -> PostCategory.ALL
                "자유게시판" -> PostCategory.FREE
                "칼럼게시판" -> PostCategory.COLUMN
                "건의게시판" -> PostCategory.SUGGESTION
                else -> PostCategory.ALL
        }
}
