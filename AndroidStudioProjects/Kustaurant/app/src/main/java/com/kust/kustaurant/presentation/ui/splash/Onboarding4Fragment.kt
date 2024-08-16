package com.kust.kustaurant.presentation.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kust.kustaurant.databinding.FragmentOnboarding4Binding

class Onboarding4Fragment : Fragment() {
    lateinit var binding: FragmentOnboarding4Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnboarding4Binding.inflate(layoutInflater)

        return binding.root
    }
}