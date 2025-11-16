package com.kust.kustaurant.domain.common.appEvent

import com.kust.kustaurant.domain.model.appEvent.AppEvent
import com.kust.kustaurant.domain.model.appEvent.LogoutReason
import kotlinx.coroutines.flow.SharedFlow

interface AppEvents {
    val serviceDown: SharedFlow<AppEvent.ServiceDown>
    val session: SharedFlow<AppEvent.Session>

    fun emitServiceDown(code: String?, msg: String?)
    fun emitLogout(reason: LogoutReason)
}

