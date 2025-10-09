package com.kust.kustaurant.domain.model.community

enum class LikeEvent(val value: String) {
    LIKE("LIKE"),
    DISLIKE("DISLIKE");

    companion object {
        fun from(value: String?): LikeEvent? = when (value?.trim()?.uppercase()) {
            "LIKE" -> LIKE
            "DISLIKE" -> DISLIKE
            else -> null
        }
    }
}
