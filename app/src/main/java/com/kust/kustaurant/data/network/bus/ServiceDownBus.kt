package com.kust.kustaurant.data.network.bus

import com.kust.kustaurant.domain.common.notice.ServiceDownNotifier
import com.kust.kustaurant.domain.model.ServiceDownEvent
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

@Singleton
class ServiceDownBus @Inject constructor() : ServiceDownNotifier {
    private val seq = java.util.concurrent.atomic.AtomicLong(0)
    private val _events = MutableSharedFlow<ServiceDownEvent>(
        replay = 1,
        extraBufferCapacity = 0
    )
    override val events: SharedFlow<ServiceDownEvent> = _events
    override fun notify503(event: ServiceDownEvent) {
        _events.tryEmit(event.copy(seq = seq.incrementAndGet()))
    }
}
