package com.kust.kustaurant.domain.common.session

import com.kust.kustaurant.domain.model.appEvent.LogoutReason

interface SessionController {
    fun logout(reason: LogoutReason)
    fun onTokenRefreshed(newAccessToken: String)
}
