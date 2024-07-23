package com.example.kustaurant.presentation.ui.detail

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MeReVPAdapter(activity : FragmentActivity) : FragmentStateAdapter(activity){

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> DetailMenuFragment()
            else -> DetailReviewFragment()
        }
    }
}