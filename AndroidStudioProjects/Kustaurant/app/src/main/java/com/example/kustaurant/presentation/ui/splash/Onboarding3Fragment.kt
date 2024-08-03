package com.example.kustaurant.presentation.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kustaurant.databinding.FragmentOnboarding3Binding

class Onboarding3Fragment : Fragment() {
    lateinit var binding: FragmentOnboarding3Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnboarding3Binding.inflate(layoutInflater)

        return binding.root
    }
}