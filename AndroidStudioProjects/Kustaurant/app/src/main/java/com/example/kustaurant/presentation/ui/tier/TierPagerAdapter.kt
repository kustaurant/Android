package com.example.kustaurant.presentation.ui.tier

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.kustaurant.TierListFragment

class TierPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TierListFragment()
            1 -> TierMapFragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}