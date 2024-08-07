package com.example.kustaurant.presentation.ui.tier

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
class TierPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                TierListFragment()
            }
            1 -> TierMapFragment()
            else -> throw IllegalStateException("Unexpected position: $position")
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshAllFragments() {
        notifyDataSetChanged()
    }
}
