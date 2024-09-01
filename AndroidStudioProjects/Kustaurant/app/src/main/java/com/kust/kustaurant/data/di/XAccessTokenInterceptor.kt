package com.kust.kustaurant.data.di

import android.content.Context
import android.util.Log
import com.kust.kustaurant.data.getAccessToken
import com.kust.kustaurant.data.saveAccessToken
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject

class XAccessTokenInterceptor(val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        var response = chain.proceed(request.newBuilder().addHeader(AUTHORIZATION, getAccessToken(context) ?: "").build())

        if (response.code == 400) {
            synchronized(this) {
                // 다시 한번 토큰 만료를 확인하여 동시에 여러 요청이 같은 토큰으로 갱신되는 것을 방지
                val currentToken = getAccessToken(context)
                if (currentToken != null && currentToken == request.header(AUTHORIZATION)) {
                    val newToken = refreshToken(currentToken) // 토큰 갱신 로직을 구현해야 함
                    if (newToken != null) {
                        saveAccessToken(context, newToken)
                        // 새 토큰으로 요청 재구성
                        request = request.newBuilder()
                            .header(AUTHORIZATION, "Bearer $newToken")
                            .build()
                        response.close() // 기존 응답 닫기
                        response = chain.proceed(request) // 새 요청 실행
                    }
                }
            }
        }

        return response
    }

    companion object {
        const val AUTHORIZATION = "Authorization"
    }

    private fun refreshToken(oldToken: String?): String? {
        val refreshTokenRequest = Request.Builder()
            .url("http://3.35.154.191:8080/api/v1/new-access-token")
            .post(RequestBody.create("application/json".toMediaTypeOrNull(), ""))
            .header("Authorization", oldToken ?: "")
            .build()

        val client = OkHttpClient()
        try {
            val response = client.newCall(refreshTokenRequest).execute()
            return if (response.isSuccessful) {
                val body = response.body?.string()
                JSONObject(body).getString("accessToken")
            } else {
                Log.e("TokenAuthenticator", "Failed to refresh token: ${response.body?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("TokenAuthenticator", "Error during token refresh", e)
            return null
        }
    }
}
