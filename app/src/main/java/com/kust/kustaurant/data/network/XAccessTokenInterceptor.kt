package com.kust.kustaurant.data.network

import android.content.Context
import android.util.Log
import com.kust.kustaurant.data.getAccessToken
import okhttp3.Interceptor
import okhttp3.Response

class XAccessTokenInterceptor(val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        val accessToken = getAccessToken(context)

        accessToken?.let {
            builder.addHeader("Authorization", it)
            Log.d("NetworkInterceptor", "Authorization: $it")
        }

        return chain.proceed(builder.build())
    }

    companion object {
        const val AUTHORIZATION = "Authorization"
    }
}
