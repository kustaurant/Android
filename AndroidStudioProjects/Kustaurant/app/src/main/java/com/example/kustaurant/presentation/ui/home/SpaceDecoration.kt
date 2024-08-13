package com.example.kustaurant.presentation.ui.home

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpaceDecoration(private val size: Int, private val edgeMargin: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount ?: 0

        if (position != RecyclerView.NO_POSITION) {
            if (position == 0) {
                outRect.left = edgeMargin
            } else {
                outRect.left = size
            }

            if (position == itemCount - 1) {
                outRect.right = edgeMargin
            } else {
                outRect.right = size
            }
        }
    }
}