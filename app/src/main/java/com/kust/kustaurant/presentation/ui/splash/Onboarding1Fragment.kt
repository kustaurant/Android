package com.kust.kustaurant.presentation.ui.splash

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.kust.kustaurant.R
import com.kust.kustaurant.databinding.FragmentOnboarding1Binding

class Onboarding1Fragment : Fragment(R.layout.fragment_onboarding1) {
    private lateinit var binding: FragmentOnboarding1Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnboarding1Binding.inflate(inflater,container,false)
        loadImage()

        return binding.root
    }

    private fun loadImage(){
        Glide.with(requireContext())
            .load("https://kustaurant.s3.ap-northeast-2.amazonaws.com/common/on-boarding/on1.png")
            .into(binding.onboarding1Iv)
    }
}