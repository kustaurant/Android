package com.example.kustaurant.presentation.ui.splash

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.kustaurant.BuildConfig
import com.example.kustaurant.MainActivity
import com.example.kustaurant.R
import com.example.kustaurant.databinding.ActivityOnboardingBinding
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApi
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse

class OnboardingFragment1: Fragment(R.layout.fragment_onboarding1)
class OnboardingFragment2: Fragment(R.layout.fragment_onboarding2)
class OnboardingFragment3: Fragment(R.layout.fragment_onboarding3)
class OnboardingFragment4: Fragment(R.layout.fragment_onboarding4)
class OnboardingActivity : AppCompatActivity() {
    lateinit var binding: ActivityOnboardingBinding
    private var selectedColor: Int = 0
    private var defaultColor: Int = 0
    private var selectedIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        selectedColor = ContextCompat.getColor(this, R.color.cement_4)
        defaultColor = ContextCompat.getColor(this, R.color.cement_3)
        setupButtons()

        val adapter = OnboardingPagerAdapter(this)
        binding.onboardingVp.adapter = adapter

        binding.onboardingVp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateButtonState(position)
            }
        })

        binding.onboardingBtn1.setOnClickListener {
            binding.onboardingVp.currentItem = 0
        }

        binding.onboardingBtn2.setOnClickListener {
            binding.onboardingVp.currentItem = 1
        }

        binding.onboardingBtn3.setOnClickListener {
            binding.onboardingVp.currentItem = 2
        }

        binding.onboardingBtn4.setOnClickListener {
            binding.onboardingVp.currentItem = 3
        }

        binding.onboardingTvSkip.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


        // naver 로그인
        val naverClientId = BuildConfig.NAVER_CLIENT_ID
        val naverClientSecret = BuildConfig.NAVER_CLIENT_SECRET
        val naverClientName = getString(R.string.naver_login_client_name)
        NaverIdLoginSDK.initialize(this,naverClientId, naverClientSecret, naverClientName)

        binding.onboardingIvNaver.setOnClickListener{
            startNaverLogin()
        }

        // kakao 로그인
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_KEY)

        binding.onboardingIvKakao.setOnClickListener {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    Log.e("kakao", "로그인 실패", error)
                }
                else if (token != null) {
                    Log.i("kakao", "로그인 성공 ${token.accessToken}")
                    UserApiClient.instance.me { user, error ->
                        Toast.makeText(this,"${user?.id}",Toast.LENGTH_LONG).show()
                    }
                    val intent = Intent(this@OnboardingActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun startNaverLogin(){
        var naverToken : String? = ""

        val profileCallback = object : NidProfileCallback<NidProfileResponse> {
            override fun onSuccess(response: NidProfileResponse) {
                val userId = response.profile?.id
                Toast.makeText(this@OnboardingActivity, "${userId}님 로그인 성공",Toast.LENGTH_SHORT).show()
                val intent = Intent(this@OnboardingActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Toast.makeText(this@OnboardingActivity,"errorcode: ${errorCode}\nerrorDescription: ${errorDescription}", Toast.LENGTH_SHORT).show()
            }

            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode,message)
            }
        }

        val oauthLoginCallback = object : OAuthLoginCallback {
            override fun onSuccess() {
                naverToken = NaverIdLoginSDK.getAccessToken()
                NidOAuthLogin().callProfileApi(profileCallback)
            }

            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Toast.makeText(this@OnboardingActivity, "errorCode: ${errorCode}\n" +
                        "errorDescription: ${errorDescription}", Toast.LENGTH_SHORT).show()
            }

            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }

        NaverIdLoginSDK.authenticate(this,oauthLoginCallback)
    }

    private fun setupButtons() {
        val buttons = listOf(
            binding.onboardingBtn1,
            binding.onboardingBtn2,
            binding.onboardingBtn3,
            binding.onboardingBtn4
        )

        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                binding.onboardingVp.currentItem = index
                updateButtonState(index)
            }
        }

        updateButtonState(selectedIndex)
    }

    private fun updateButtonState(selectedIndex: Int) {
        val buttons = listOf(
            binding.onboardingBtn1,
            binding.onboardingBtn2,
            binding.onboardingBtn3,
            binding.onboardingBtn4
        )

        buttons.forEachIndexed { index, button ->
            if (index == selectedIndex) {
                button.layoutParams.width = dpToPx(16)
                button.layoutParams.height = dpToPx(16)
                button.isSelected = true
                button.requestLayout()
            } else {
                button.layoutParams.width = dpToPx(12)
                button.layoutParams.height = dpToPx(12)
                button.isSelected = false
                button.requestLayout()
            }
        }
    }

    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }
}