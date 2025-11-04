package com.kust.kustaurant.domain.common.notice

import com.kust.kustaurant.domain.model.ServiceDownEvent
import kotlinx.coroutines.flow.SharedFlow

interface ServiceDownNotifier {
    val events: SharedFlow<ServiceDownEvent>
    fun notify503(event: ServiceDownEvent)
}
