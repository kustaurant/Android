package com.kust.kustaurant.data.local

import android.content.SharedPreferences
import com.kust.kustaurant.data.datasource.AuthPreferenceDataSource
import javax.inject.Inject

class AuthPreferenceDataSourceImpl @Inject constructor(
    private val prefs: SharedPreferences
) : AuthPreferenceDataSource {
    override fun setAccessToken(token: String) {
        prefs.edit()
            .putString("access_token", "Bearer $token")
            .apply()
    }

    override fun getAccessToken(): String? =
        prefs.getString("access_token", null)

    override fun setRefreshToken(token: String) {
        prefs.edit()
            .putString("refresh_token", token)
            .apply()
    }

    override fun getRefreshToken(): String? =
        prefs.getString("refresh_token", null)

    override fun setUserId(userId: String) {
        prefs.edit()
            .putString("userId", userId)
            .apply()
    }

    override fun getUserId(): String? =
        prefs.getString("userId", null)

    override fun setDeviceId(deviceId: String) {
        prefs.edit()
            .putString("device_id", deviceId)
            .apply()
    }

    override fun getDeviceId(): String? =
        prefs.getString("device_id", null)

    override fun clearUserTokens() {
        prefs.edit()
            .remove("access_token")
            .remove("refresh_token")
            .remove("userId")
            .apply()
    }
}
