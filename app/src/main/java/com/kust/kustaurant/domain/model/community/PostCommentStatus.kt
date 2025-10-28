package com.kust.kustaurant.domain.model.community

enum class PostCommentStatus(val value: String) {
    ACTIVE("ACTIVE"),
    PENDING("PENDING"),
    DELETED("DELETED");

    companion object {
        fun from(value: String?): PostCommentStatus =
            entries.find { it.value.equals(value, ignoreCase = true) } ?: ACTIVE
    }
}
