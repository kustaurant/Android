package com.kust.kustaurant.data.network

import com.kust.kustaurant.domain.common.appEvent.AppEvents
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ServiceUnavailableNotifyInterceptor @Inject constructor(
    private val appEvent: AppEvents
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val resp = chain.proceed(chain.request())

        if (resp.code == 503) {
            val bodyStr = runCatching { resp.peekBody(64 * 1024).string() }.getOrNull()
            val (code, message) = parsePayload(bodyStr)

            if (code.equals("MAINTENANCE", ignoreCase = true)) {
                appEvent.emitServiceDown(
                        code = code,
                        msg = message ?: "현재 앱 업데이트 중입니다. 웹 서비스는 정상적으로 이용 가능합니다. 빠른 시일 내에 찾아뵙겠습니다."
                    )
            }
        }
        return resp
    }

    private fun parsePayload(raw: String?): Pair<String?, String?> {
        if (raw.isNullOrBlank()) return null to null
        return runCatching {
            val j = org.json.JSONObject(raw)
            j.optString("code", null) to j.optString("message", null)
        }.getOrDefault(null to null)
    }
}

