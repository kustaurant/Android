package com.kust.kustaurant.data.di

import android.content.Context
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
        val token = getAccessToken(context)  // 기존 토큰 가져오기
        val newToken = refreshToken(token)  // 토큰 갱신 로직
        return if (newToken != null) {
            saveAccessToken(context, newToken)
            response.request.newBuilder()
                .header("Authorization", "Bearer $newToken")
                .build()
        } else {
            null
        }
    }

    private fun refreshToken(oldToken: String?): String? {
        val refreshTokenRequest = Request.Builder()
            .url("http://3.35.154.191:8080/api/v1/new-access-token")
            .post(RequestBody.create("application/json".toMediaTypeOrNull(), ""))
            .header("Authorization", oldToken ?: "")
            .build()

        val client = OkHttpClient()
        val response = client.newCall(refreshTokenRequest).execute()
        return if (response.isSuccessful) {
            val body = response.body?.string()
            JSONObject(body).getString("new_access_token")  // 서버 응답 형식에 맞게 수정
        } else {
            null
        }
    }
}
