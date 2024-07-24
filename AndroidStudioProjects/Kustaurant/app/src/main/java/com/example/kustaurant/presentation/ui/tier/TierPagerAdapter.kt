package com.example.kustaurant.presentation.ui.tier

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class TierPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TierListFragment()
            1 -> TierMapFragment()
            2 -> TierCategoryFragment()
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}
