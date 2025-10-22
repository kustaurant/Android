package com.kust.kustaurant.data.network

import android.content.Context
import com.kust.kustaurant.data.getAccessToken
import com.kust.kustaurant.data.getDeviceId
import com.kust.kustaurant.data.saveDeviceId
import okhttp3.Interceptor
import okhttp3.Response

class XAccessTokenInterceptor(val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val builder = original.newBuilder()

        val access = getAccessToken(context)
        if (!access.isNullOrBlank()) {
            builder.header("Authorization", ensureBearer(access))
        } else {
            getDeviceId(context)?.let { builder.header("X-Device-Id", it) }
        }

        val response = chain.proceed(builder.build())
        response.header("X-Anonymous-Id")?.let { anon ->
            if (anon.isNotBlank() && anon != getDeviceId(context)) {
                saveDeviceId(context, anon)
            }
        }

        return response
    }

    private fun ensureBearer(token: String): String =
        if (token.startsWith("Bearer ")) token else "Bearer $token"
}
