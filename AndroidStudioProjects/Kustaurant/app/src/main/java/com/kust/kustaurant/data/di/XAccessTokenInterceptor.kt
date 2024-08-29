package com.kust.kustaurant.data.di

import android.content.Context
import android.util.Log
import com.kust.kustaurant.data.getAccessToken
import com.kust.kustaurant.data.saveAccessToken
import com.kust.kustaurant.presentation.ui.mypage.NewAccessTokenViewModel
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class XAccessTokenInterceptor(
    val context: Context
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        var accessToken = getAccessToken(context)

        accessToken?.let {
            builder.addHeader("Authorization", it)
            Log.d("NetworkInterceptor", "Authorization: $it")
        }
//
//        var response = chain.proceed(builder.build())
//
//        if(response.code == 400 || response.code == 403){
//            response.close()
//
//            runBlocking {
//                newAccessTokenViewModel.postNewAccessToken(context)
//                val newToken = newAccessTokenViewModel.accessToken.value
//                newToken?.let {
//                    saveAccessToken(context, it)
//                    accessToken = it
//                }
//            }
//
//            val newRequest = chain.request().newBuilder()
//                .header(AUTHORIZATION, accessToken ?: "")
//                .build()
//
//            response = chain.proceed(newRequest)
//
//        }
//
//        return response
        return chain.proceed(builder.build())
    }

    companion object {
        const val AUTHORIZATION = "Authorization"
    }
}
