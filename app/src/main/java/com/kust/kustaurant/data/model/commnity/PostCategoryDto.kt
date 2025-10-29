package com.kust.kustaurant.data.model.commnity

import com.google.gson.annotations.SerializedName
import com.kust.kustaurant.domain.model.community.PostCategory

enum class PostCategoryDto {
    @SerializedName("전체게시판")
    ALL,
    @SerializedName("자유게시판")
    FREE,
    @SerializedName("칼럼게시판")
    COLUMN,
    @SerializedName("건의게시판")
    SUGGESTION;

    fun toDomain(): PostCategory = when (this) {
        ALL -> PostCategory.ALL
        FREE -> PostCategory.FREE
        COLUMN -> PostCategory.COLUMN
        SUGGESTION -> PostCategory.SUGGESTION
    }
}