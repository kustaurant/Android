package com.kust.kustaurant.presentation.ui.community

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
class CommunityPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CommunityPostListFragment()

            1 -> CommunityPostRankingFragment()

            else -> throw IllegalStateException("Unexpected position: $position")
        }
    }

}
