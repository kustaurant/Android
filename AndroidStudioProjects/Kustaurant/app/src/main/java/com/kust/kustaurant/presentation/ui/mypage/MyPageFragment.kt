package com.kust.kustaurant.presentation.ui.mypage

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyPageBinding.inflate(layoutInflater)
        binding.viewModel = myPageViewModel
        binding.lifecycleOwner = this

        myPageViewModel.loadMyPageData()

        initButtons()

        logoutViewModel.Response.observe(viewLifecycleOwner) { response ->
            if (response == "로그아웃이 완료되었습니다.") {
                // 로그아웃 성공 시 토큰과 ID 초기화
                clearUserData()

                val intent = Intent(requireContext(), StartActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }

        return binding.root
    }

    private fun initButtons() {
        val buttonActions = mapOf(
            binding.myIvEdit to MyEditActivity::class.java,
            binding.myTvOwnerCertificate to MyCertificateActivity::class.java,
            binding.myTvFixAlliance to MyFixAllActivity::class.java,
            binding.myTvSave to MySaveActivity::class.java,
            binding.myTvCommunityComment to MyCommentActivity::class.java,
            binding.myTvCommunityScrap to MyScrapActivity::class.java,
            binding.myTvOpinion to MyOpinionActivity::class.java,
            binding.myTvLogOut to null
        )

        buttonActions.forEach { (button, activityClass) ->
            button.setOnClickListener {
                if (activityClass != null) {
                    startActivity(activityClass)
                } else {
                    initLogOut()
                }
            }
        }
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
        logoutViewModel.postLogout() ?: Log.d("Logout", "accessToken 비어있음")
    }
}