package com.kust.kustaurant.presentation.common

import android.content.Context

object DimensionUtils {

    fun Context.dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }

    fun Context.pxToDp(px: Int): Float {
        return px / resources.displayMetrics.density
    }
}