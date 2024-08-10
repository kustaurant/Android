package com.example.kustaurant.presentation.ui.detail

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemDecoration(private val spacing: Int, private val endSpacing: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.bottom = spacing
        if (parent.getChildAdapterPosition(view) == parent.adapter!!.itemCount - 1) {

            // 마지막 아이템에만 추가적인 마진 적용
            outRect.bottom += endSpacing
        }
    }
}