package com.kust.kustaurant.data.network

import com.kust.kustaurant.data.datasource.AuthPreferenceDataSource
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class XAccessTokenInterceptor @Inject constructor(
    private val authPreferenceDataSource: AuthPreferenceDataSource
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val builder = original.newBuilder()

        val accessToken = authPreferenceDataSource.getAccessToken()
        if (!accessToken.isNullOrBlank()) {
            builder.header("Authorization", ensureBearer(accessToken))
        } else {
            authPreferenceDataSource.getDeviceId()?.let { deviceId ->
                builder.header("X-Device-Id", deviceId)
            }
        }

        val response = chain.proceed(builder.build())

        response.header("X-Anonymous-Id")?.let { anon ->
            if (anon.isNotBlank() && anon != authPreferenceDataSource.getDeviceId()) {
                authPreferenceDataSource.setDeviceId(anon)
            }
        }

        return response
    }

    private fun ensureBearer(token: String): String =
        if (token.startsWith("Bearer ")) token else "Bearer $token"
}

