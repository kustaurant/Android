package com.example.kustaurant.presentation.ui.splash

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class OnboardingPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    private val fragments = listOf(
        OnboardingFragment1(),
        OnboardingFragment2(),
        OnboardingFragment3(),
        OnboardingFragment4()
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}