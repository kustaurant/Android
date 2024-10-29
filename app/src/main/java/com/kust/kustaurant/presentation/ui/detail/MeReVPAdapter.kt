package com.kust.kustaurant.presentation.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MeReVPAdapter(activity : FragmentActivity, private val restaurantId : Int) : FragmentStateAdapter(activity){

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        val fragment = when(position) {
            0 -> DetailMenuFragment()
            else -> DetailReviewFragment()
        }

        val args = Bundle()
        args.putInt("restaurantId", restaurantId)
        fragment.arguments = args
        return fragment
    }
}