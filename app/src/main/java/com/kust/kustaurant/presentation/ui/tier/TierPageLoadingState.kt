package com.kust.kustaurant.presentation.ui.tier

enum class TierPhase { Idle, Refreshing, Paging }

data class TierPageState(
    val phase: TierPhase = TierPhase.Idle,
    val page: Int = 1,
    val isLastPage: Boolean = false
)

