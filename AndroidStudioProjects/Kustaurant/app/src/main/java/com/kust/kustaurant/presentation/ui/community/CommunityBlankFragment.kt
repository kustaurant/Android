package com.kust.kustaurant.presentation.ui.community

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kust.kustaurant.R
import com.kust.kustaurant.databinding.FragmentCommunityBinding
import com.kust.kustaurant.databinding.FragmentCommunityBlankBinding
import com.kust.kustaurant.presentation.ui.search.SearchActivity

class CommunityBlankFragment : Fragment() {
    lateinit var binding : FragmentCommunityBlankBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommunityBlankBinding.inflate(layoutInflater)

        initSearch()
        return binding.root
    }

    private fun initSearch() {
        binding.communityFlIvSearch.setOnClickListener {
            val intent = Intent(requireContext(), SearchActivity::class.java)
            startActivity(intent)
        }
    }

}