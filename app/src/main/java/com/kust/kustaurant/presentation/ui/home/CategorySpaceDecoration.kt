package com.kust.kustaurant.presentation.ui.home

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CategorySpaceDecoration(
    private val spanCount: Int, // 열 개수
    private val spacing: Int // 아이템 간 마진
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view) // 아이템 위치
        val column = position % spanCount // 현재 열

        // 첫 번째 아이템과 마지막 아이템에는 각각 왼쪽과 오른쪽 마진을 주지 않음
        when (column) {
            0 -> {
                outRect.right = spacing / 2 // 첫 번째 아이템
            }
            spanCount - 1 -> {
                outRect.left = spacing / 2 // 마지막 아이템
            }
            else -> {
                outRect.left = spacing / 2 // 각 마진 1/2 로 양옆
                outRect.right = spacing / 2
            }
        }

        // 첫 번째 행이 아닐 경우 상단 마진 추가
        if (position >= spanCount) {
            outRect.top = spacing
        }
    }
}

