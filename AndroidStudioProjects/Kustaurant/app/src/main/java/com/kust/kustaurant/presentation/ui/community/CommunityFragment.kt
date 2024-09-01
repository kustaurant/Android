package com.kust.kustaurant.presentation.ui.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.kust.kustaurant.databinding.FragmentCommunityBinding

class CommunityFragment  : Fragment() {
    private var _binding: FragmentCommunityBinding? = null
    private val viewModel: CommunityViewModel by activityViewModels()
    val binding get() = _binding!!

    private lateinit var pagerAdapter: CommunityPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommunityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        setupTabLayout()
    }

    private fun setupViewPager() {
        pagerAdapter = CommunityPagerAdapter(this)
        binding.communityViewPager.adapter = pagerAdapter
    }

    private fun setupTabLayout() {
        binding.communityTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.communityViewPager.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        binding.communityViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.communityTabLayout.selectTab(binding.communityTabLayout.getTabAt(position))
            }
        })

        binding.communityTabLayout.addTab(binding.communityTabLayout.newTab().setText("게시글"))
        binding.communityTabLayout.addTab(binding.communityTabLayout.newTab().setText("평가 랭킹"))
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}