package com.kust.kustaurant.presentation.ui.mypage

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.kust.kustaurant.R
import com.kust.kustaurant.data.getAccessToken
import com.kust.kustaurant.data.saveAccessToken
import com.kust.kustaurant.databinding.FragmentMyPageBinding
import com.kust.kustaurant.presentation.ui.splash.StartActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : Fragment() {
    lateinit var binding: FragmentMyPageBinding
    private val logoutViewModel: LogoutViewModel by viewModels()
    private val myPageViewModel: MyPageViewModel by viewModels()
    private val goodByeViewModel: GoodByeViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyPageBinding.inflate(layoutInflater)
        binding.viewModel = myPageViewModel
        binding.lifecycleOwner = this

        val accessToken = getAccessToken(requireContext())

        if (accessToken != null) {
            myPageViewModel.loadMyPageData()
            initButtons()
        } else {
            binding.myIvEdit.visibility = View.GONE
            binding.myIvLogIn.visibility = View.VISIBLE
            binding.myIvUser.setImageResource(R.drawable.ic_none_user)
            initLogIn()
            disableButtons()
        }

        return binding.root
    }

    private fun initLogIn() {
        binding.myIvLogIn.setOnClickListener {
            val intent = Intent(requireContext(), StartActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initButtons() {
        val buttonActions = mapOf(
            binding.myClEvaluateCount to MyEvaluateActivity::class.java,
//            binding.myClPostCount to MyPostActivity::class.java,
            binding.myClUserInfo to MyEditActivity::class.java,
            binding.myTvSave to MySaveActivity::class.java,
//            binding.myTvCommunityComment to MyCommentActivity::class.java,
//            binding.myTvCommunityScrap to MyScrapActivity::class.java,
            binding.myTvTermsUse to MyTermsUseActivity::class.java,
            binding.myTvOpinion to MyOpinionActivity::class.java,
            binding.myTvNotification to MyNotificationActivity::class.java,
            binding.myTvPrivatePolicy to MyPrivacyPolicyActivity::class.java,
            // 점주 인증, 제휴는 추후 업데이트
//            binding.myTvOwnerCertificate to MyCertificateActivity::class.java,
//            binding.myTvFixAlliance to MyFixAllActivity::class.java,
            binding.myTvLogOut to null,
            binding.myTvWithdrawal to null
        )

        buttonActions.forEach { (button, activityClass) ->
            button.setOnClickListener {
                if (activityClass != null) {
                    startActivity(activityClass)
                } else {
                    when (button.id) {
                        binding.myTvLogOut.id -> initLogOut() // 로그아웃 버튼
                        binding.myTvWithdrawal.id -> initWithdrawal() // 회원탈퇴 버튼
                    }
                }
            }
        }
    }

    private fun disableButtons() {
        val textViews = listOf(
            binding.myTvNotification,
            binding.myTvOwnerCertificate,
            binding.myTvFixAlliance,
            binding.myTvSave,
            binding.myTvTermsUse,
            binding.myTvPrivatePolicy,
            binding.myTvCommunityComment,
            binding.myTvCommunityScrap,
            binding.myTvOpinion,
            binding.myTvLogOut,
            binding.myTvWithdrawal
        )

        textViews.forEach { textView ->
            textView.isEnabled = false
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.cement_4))
        }

        binding.myClPostCount.isEnabled = false
        binding.myClEvaluateCount.isEnabled = false
    }

    private fun clearUserData() {
        requireContext().getSharedPreferences("app_preferences", MODE_PRIVATE).edit().apply {
            remove("access_token")
            remove("userId")
            apply()
        }
    }

    private fun startActivity(targetActivity: Class<*>) {
        val intent = Intent(requireContext(), targetActivity)
        startActivity(intent)
    }


    private fun initLogOut() {
        logoutViewModel.postLogout()

        logoutViewModel.response.observe(viewLifecycleOwner) { result ->
            when (result) {
                "success" -> {
                    // 로그아웃 성공 시 토큰과 ID 초기화
                    clearUserData()

                    val intent = Intent(requireContext(), StartActivity::class.java)
                    startActivity(intent)
                    onDestroy()
                }
                "fail" -> {
                    // 로그아웃 실패 시 처리
                    Toast.makeText(requireContext(), "로그아웃 실패: 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // 기타 경우의 처리
                    Toast.makeText(requireContext(), "알 수 없는 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun initWithdrawal() {
        goodByeViewModel.postGoodBye()

        goodByeViewModel.response.observe(viewLifecycleOwner) { result ->
            when (result) {
                "success" -> {
                    clearUserData()
                    val intent = Intent(requireContext(), StartActivity::class.java)
                    startActivity(intent)
                    onDestroy()
                }
                "fail" -> {
                    Toast.makeText(requireContext(), "회원탈퇴 실패: 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(requireContext(), "알 수 없는 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}