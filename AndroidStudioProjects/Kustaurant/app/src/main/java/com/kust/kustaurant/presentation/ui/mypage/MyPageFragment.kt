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
import com.kust.kustaurant.databinding.FragmentMyPageBinding
import com.kust.kustaurant.presentation.ui.splash.StartActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : Fragment() {
    lateinit var binding : FragmentMyPageBinding
    private val logoutViewModel: LogoutViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyPageBinding.inflate(layoutInflater)

        binding.myIvEdit.setOnClickListener {
            initEdit()
        }
        binding.myTvOwnerCertificate.setOnClickListener {
            initOwnerCertificate()
        }
        binding.myTvFixAlliance.setOnClickListener {
            initFixAlliance()
        }
        binding.myTvSave.setOnClickListener {
            initSave()
        }
        binding.myTvCommunityComment.setOnClickListener {
            initCommunityComment()
        }
        binding.myTvCommunityScrap.setOnClickListener {
            initCommunityScrap()
        }
        binding.myTvOpinion.setOnClickListener {
            initOpinion()
        }
        binding.myTvLogOut.setOnClickListener {
            initLogOut()
        }
        logoutViewModel.Response.observe(viewLifecycleOwner){response->
            if (response == "로그아웃이 완료되었습니다."){
                // 로그아웃 성공 시 토큰과 ID 초기화
                clearUserData()

                val intent = Intent(requireContext(), StartActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }
        binding.myTvNotification.setOnClickListener {
            initNotification()
        }
        return binding.root
    }

    private fun clearUserData() {
        val preferences = requireContext().getSharedPreferences("app_preferences", MODE_PRIVATE)
        val editor = preferences.edit()
        editor.remove("access_token")
        editor.remove("userId")
        editor.apply()
    }

    private fun initEdit() {
        val intent = Intent(requireContext(), MyEditActivity::class.java)
        startActivity(intent)
    }

    private fun initNotification() {
        val intent = Intent(requireContext(), MyNotificationActivity::class.java)
        startActivity(intent)
    }

    private fun getAccessToken(): String? {
        val preferences = requireContext().getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        return preferences.getString("access_token", null)
    }

    private fun initLogOut() {
        val accessToken = getAccessToken()
        Log.d("logout","${accessToken}")
        if (accessToken!=null){
            val authorizationHeader = "Bearer $accessToken"
            logoutViewModel.postLogout(authorizationHeader)
        }else{
            Log.d("Logout","accessToken 비어있음")
        }
    }

    private fun initOwnerCertificate() {
        val intent = Intent(requireContext(), MyCertificateActivity::class.java)
        startActivity(intent)
    }

    private fun initFixAlliance() {
        val intent = Intent(requireContext(), MyFixAllActivity::class.java)
        startActivity(intent)
    }

    private fun initSave() {
        val intent = Intent(requireContext(), MySaveActivity::class.java)
        startActivity(intent)
    }

    private fun initCommunityComment() {
        val intent = Intent(requireContext(), MyCommentActivity::class.java)
        startActivity(intent)
    }

    private fun initCommunityScrap() {
        val intent = Intent(requireContext(), MyScrapActivity::class.java)
        startActivity(intent)
    }

    private fun initOpinion() {
        val intent = Intent(requireContext(), MyOpinionActivity::class.java)
        startActivity(intent)
    }
}