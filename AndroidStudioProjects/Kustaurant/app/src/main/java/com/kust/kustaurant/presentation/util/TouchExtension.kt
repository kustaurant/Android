package com.kust.kustaurant.presentation.util

import android.graphics.Rect
import android.view.TouchDelegate
import android.view.View

object TouchExtension {
    // 사용방법 : TouchDelegateUtil.expandTouchArea(parentView, iconView, 10)
    // parentView: 해당 아이콘 상위뷰, iconView: 해당 아이콘, extraPadding : 터치 늘릴 padding 값(dp값 넣어 주세요)
    fun expandTouchArea(parentView: View, iconView: View, extraPadding: Int) {
        parentView.post {
            val rect = Rect()
            iconView.getHitRect(rect)

            // top과 left는 -가 확장, bottom과 right는 +가 확장입니다.
            rect.top -= extraPadding
            rect.bottom += extraPadding
            rect.left -= extraPadding
            rect.right += extraPadding

            val touchDelegate = TouchDelegate(rect, iconView)
            (iconView.parent as? View)?.touchDelegate = touchDelegate
        }
    }
}

