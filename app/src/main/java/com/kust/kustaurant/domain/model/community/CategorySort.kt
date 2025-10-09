package com.kust.kustaurant.domain.model.community

enum class CategorySort(val value : String) {
    POPULARITY("POPULARITY"),
    LATEST("LATEST")
}

fun String.toCategorySort(): CategorySort {
    return when (this) {
        "recent" -> CategorySort.LATEST
        "popular" -> CategorySort.POPULARITY
        else -> CategorySort.POPULARITY
    }
}