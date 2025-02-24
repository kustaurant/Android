package com.kust.kustaurant.presentation.ui.tier

enum class TierScreenType(val idx : Int) {
    LIST(0),
    MAP(1);

    companion object {
        fun fromTabIdx(idx : Int) : TierScreenType {
            return when(idx) {
                0 -> LIST
                1 -> MAP
                else -> throw IllegalArgumentException("Invalid tab index")
            }
        }
    }
}