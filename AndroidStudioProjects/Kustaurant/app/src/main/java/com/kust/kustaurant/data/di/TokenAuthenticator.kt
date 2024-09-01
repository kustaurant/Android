package com.kust.kustaurant.data.di

import android.content.Context
import android.util.Log
import com.kust.kustaurant.data.getAccessToken
import com.kust.kustaurant.data.model.LoginResponse
import com.kust.kustaurant.data.remote.NewAccessTokenApi
import com.kust.kustaurant.data.saveAccessToken
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.Route
import org.json.JSONObject

class TokenAuthenticator(private val context: Context) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        // 400 또는 403 응답을 만료된 토큰으로 간주
        if (response.code == 400 || response.code == 403) {
            val token = getAccessToken(context)  // 기존 토큰 가져오기
            val newToken = refreshToken(token)  // 토큰 갱신 로직
            return if (newToken != null) {
                saveAccessToken(context, newToken)
                response.request.newBuilder()
                    .header("Authorization", "Bearer $newToken")
                    .build()
            } else {
                null  // 갱신 실패 시 null 반환
            }
        }
        return null
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
