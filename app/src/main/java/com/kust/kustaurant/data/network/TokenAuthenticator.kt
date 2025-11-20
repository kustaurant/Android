package com.kust.kustaurant.data.network

import android.util.Log
import com.kust.kustaurant.BuildConfig
import com.kust.kustaurant.data.datasource.AuthPreferenceDataSource
import com.kust.kustaurant.domain.common.session.SessionController
import com.kust.kustaurant.domain.model.appEvent.LogoutReason
import okhttp3.Authenticator
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.Route
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class TokenAuthenticator
@Inject constructor(
    private val prefs: AuthPreferenceDataSource,
    private val sessionController : SessionController
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.code == 401) {
            val token = prefs.getRefreshToken()
            val newToken = refreshToken(token)
            return if (newToken != null) {
                prefs.setAccessToken(newToken)
                response.request.newBuilder()
                    .header("Authorization", "Bearer $newToken")
                    .build()
            } else {
                sessionController.logout(LogoutReason.RefreshFailed)
                null
            }
        }
        return null
    }

    private fun refreshToken(oldToken: String?): String? {
        try {
            val refreshTokenRequest = Request.Builder()
                .url(BuildConfig.BASE_URL + "/api/v2/token/refresh")
                .post(RequestBody.create("application/json".toMediaTypeOrNull(), "{}"))
                .header("Authorization", oldToken ?: "")
                .build()

            val client = OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build()

            val response = client.newCall(refreshTokenRequest).execute()
            if (response.isSuccessful) {
                val body = response.body.string()
                return body.let { JSONObject(it).getString("accessToken") }
            } else {
                Log.e("TokenAuthenticator", "refresh 실패 코드: ${response.code}")
            }
        } catch (e: Exception) {
            Log.e("TokenAuthenticator", "refresh 실패 코드: ${e.localizedMessage}")
        }
        return null
    }
}
