package com.kust.kustaurant.data.datasource

interface AuthPreferenceDataSource {
    fun setAccessToken(token: String)
    fun getAccessToken(): String?

    fun setRefreshToken(token: String)
    fun getRefreshToken(): String?

    fun setUserId(userId: String)
    fun getUserId(): String?

    fun setDeviceId(deviceId: String)
    fun getDeviceId(): String?

    fun clearUserInfo()
}

