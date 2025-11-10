package com.kust.kustaurant.domain.model.community

enum class RankingSort(val value : String) {
    SEASONAL("SEASONAL"),
    CUMULATIVE("CUMULATIVE")
}

fun String.toRankingSort(): RankingSort {
    return when (this) {
        "seasonal" -> RankingSort.SEASONAL
        "cumulative" -> RankingSort.CUMULATIVE
        else -> RankingSort.SEASONAL
    }
}