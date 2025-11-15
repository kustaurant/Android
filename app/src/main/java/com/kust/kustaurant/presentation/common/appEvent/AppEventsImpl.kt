package com.kust.kustaurant.presentation.common.appEvent

import com.kust.kustaurant.domain.common.appEvent.AppEvents
import com.kust.kustaurant.domain.model.appEvent.AppEvent
import com.kust.kustaurant.domain.model.appEvent.LogoutReason
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppEventsImpl @Inject constructor() : AppEvents {
    private val seq = java.util.concurrent.atomic.AtomicLong(0)

    private val _serviceDown = MutableSharedFlow<AppEvent.ServiceDown>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    override val serviceDown: SharedFlow<AppEvent.ServiceDown> = _serviceDown

    private val _session = MutableSharedFlow<AppEvent.Session>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    override val session: SharedFlow<AppEvent.Session> = _session

    override fun emitServiceDown(code: String?, msg: String?) {
        _serviceDown.tryEmit(
            AppEvent.ServiceDown(
                seq = seq.incrementAndGet(),
                code = code,
                message = msg
            )
        )
    }

    override fun emitLogout(reason: LogoutReason) {
        _session.tryEmit(
            AppEvent.Session(
                seq = seq.incrementAndGet(),
                reason = reason
            )
        )
    }
}
