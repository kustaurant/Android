package com.kust.kustaurant.presentation.common.session

import com.kust.kustaurant.data.datasource.AuthPreferenceDataSource
import com.kust.kustaurant.domain.common.appEvent.AppEvents
import com.kust.kustaurant.domain.common.session.SessionController
import com.kust.kustaurant.domain.model.appEvent.LogoutReason
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionControllerImpl
@Inject constructor(
    private val prefs: AuthPreferenceDataSource,
    private val appEvents: AppEvents
) : SessionController {

    override fun logout(reason: LogoutReason) {
        prefs.clearUserInfo()
        appEvents.emitLogout(reason)
    }

    override fun onTokenRefreshed(newAccessToken: String) {
        prefs.setAccessToken(newAccessToken)
    }
}