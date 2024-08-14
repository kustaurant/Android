package com.kust.kustaurant.presentation.ui.mypage

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kust.kustaurant.databinding.FragmentMyPageBinding

class MyPageFragment : Fragment() {
    lateinit var binding : FragmentMyPageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyPageBinding.inflate(layoutInflater)


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
        return binding.root
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