package com.kust.kustaurant.domain.model.appEvent

sealed class AppEvent {
    data class ServiceDown(
        val seq: Long,
        val code: String?,
        val message: String?
    ) : AppEvent()

    data class Session(
        val seq: Long,
        val reason: LogoutReason,
    ) : AppEvent()
} 
