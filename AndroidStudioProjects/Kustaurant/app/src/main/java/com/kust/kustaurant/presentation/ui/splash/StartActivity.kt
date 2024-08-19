package com.kust.kustaurant.presentation.ui.splash

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.kust.kustaurant.BuildConfig
import com.kust.kustaurant.MainActivity
import com.kust.kustaurant.R
import com.kust.kustaurant.databinding.ActivityStartBinding
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartActivity : AppCompatActivity() {
    lateinit var binding: ActivityStartBinding
    private val naverloginviewModel: NaverLoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startTvSkip.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // naver 로그인
        val naverClientId = BuildConfig.NAVER_CLIENT_ID
        val naverClientSecret = BuildConfig.NAVER_CLIENT_SECRET
        val naverClientName = getString(R.string.naver_login_client_name)
        NaverIdLoginSDK.initialize(this,naverClientId, naverClientSecret, naverClientName)

        binding.startIvNaver.setOnClickListener{
            startNaverLogin()
        }

        naverloginviewModel.accessToken.observe(this){newAccessToken ->
            // sharedpreference를 통해 accesstoken 저장
            saveAccessToken(newAccessToken)
            val intent = Intent(this@StartActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

//        // kakao 로그인
//        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_KEY)
//
//        binding.startIvKakao.setOnClickListener {
//            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
//                if (error != null) {
//                    Log.e("kakao", "로그인 실패", error)
//                }
//                else if (token != null) {
//                    Log.i("kakao", "로그인 성공 ${token.accessToken}")
//                    UserApiClient.instance.me { user, error ->
//                        Toast.makeText(this,"${user?.id}", Toast.LENGTH_LONG).show()
//                    }
//                    val intent = Intent(this@StartActivity, MainActivity::class.java)
//                    startActivity(intent)
//                    finish()
//                }
//            }
//        }
    }

    private fun saveAccessToken(token: String) {
        val preferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("access_token", token)
        editor.apply()
    }

    private fun startNaverLogin(){
        val profileCallback = object : NidProfileCallback<NidProfileResponse> {
            override fun onSuccess(response: NidProfileResponse) {
                // provider, providerId, naveraccesstoken 설정
                val provider = "naver"
                val providerId = response.profile?.id
                val naverAccessToken = NaverIdLoginSDK.getAccessToken()

                Log.d("Naver Login", "${providerId}, ${naverAccessToken}")

                naverloginviewModel.postNaverLogin(provider, providerId ?: "", naverAccessToken?:"")
            }

            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Toast.makeText(this@StartActivity,"errorcode: ${errorCode}\nerrorDescription: ${errorDescription}", Toast.LENGTH_SHORT).show()
            }

            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode,message)
            }
        }

        val oauthLoginCallback = object : OAuthLoginCallback {
            override fun onSuccess() {
                NidOAuthLogin().callProfileApi(profileCallback)
            }

            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Toast.makeText(this@StartActivity, "errorCode: ${errorCode}\n" +
                        "errorDescription: ${errorDescription}", Toast.LENGTH_SHORT).show()
            }

            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }

        NaverIdLoginSDK.authenticate(this,oauthLoginCallback)
    }
}