package com.kust.kustaurant.presentation.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.kust.kustaurant.R
import com.kust.kustaurant.databinding.FragmentOnboarding3Binding

class Onboarding3Fragment : Fragment() {
    lateinit var binding: FragmentOnboarding3Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnboarding3Binding.inflate(layoutInflater)
        loadImage()
        return binding.root
    }

    private fun loadImage(){
        Glide.with(requireContext())
            .load(R.drawable.img_onboarding3)
            .into(binding.onboarding3Iv)
    }
}