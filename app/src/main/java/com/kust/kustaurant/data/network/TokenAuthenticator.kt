package com.kust.kustaurant.data.network

import android.content.Context
import android.content.Intent
import android.util.Log
import com.kust.kustaurant.BuildConfig
import com.kust.kustaurant.data.getAccessToken
import com.kust.kustaurant.data.saveAccessToken
import com.kust.kustaurant.presentation.ui.splash.StartActivity
import okhttp3.Authenticator
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.Route
import org.json.JSONObject

class TokenAuthenticator(private val context: Context) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.code == 401) {
            val token = getAccessToken(context)  // 기존 토큰 가져오기
            val newToken = refreshToken(token)  // 토큰 갱신 로직
            return if (newToken != null) {
                saveAccessToken(context, newToken)
                response.request.newBuilder()
                    .header("Authorization", "Bearer $newToken")
                    .build()
            } else {
                handleLogout()
                null
            }
        }
        return null
    }

    private fun refreshToken(oldToken: String?): String? {
        try {
            val refreshTokenRequest = Request.Builder()
                .url(BuildConfig.BASE_URL + "/api/v1/new-access-token")
                .post(RequestBody.create("application/json".toMediaTypeOrNull(), "{}"))
                .header("Authorization", oldToken ?: "")
                .build()

            val client = OkHttpClient()
            val response = client.newCall(refreshTokenRequest).execute()
            if (response.isSuccessful) {
                val body = response.body?.string()
                return body?.let { JSONObject(it).getString("accessToken") }
            }
        } catch (e: Exception) {
            Log.e("TokenAuthenticator", "Failed to refresh token: ${e.localizedMessage}")
        }
        return null
    }

    private fun handleLogout() {
        context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE).edit().clear().apply()

        val intent = Intent(context, StartActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        context.startActivity(intent)
    }
}
