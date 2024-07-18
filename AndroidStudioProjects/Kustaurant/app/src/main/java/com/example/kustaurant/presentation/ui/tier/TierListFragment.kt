package com.example.kustaurant.presentation.ui.tier

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kustaurant.databinding.FragmentTierListBinding

class TierListFragment : Fragment() {

    private lateinit var binding: FragmentTierListBinding
    private lateinit var tierAdapter: TierListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTierListBinding.inflate(inflater, container, false)
        setupRecyclerView()
        return binding.root
    }

    private fun setupRecyclerView() {
        tierAdapter = TierListAdapter()
        binding.recyclerviewTier.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = tierAdapter
        }

        // 예제 데이터 추가
        val exampleData = listOf(
            TierModel(1, "호야초밥참치 본점", "일식", "건입~중문", "", -1, "",
                isFavorite = true,
                isChecked = true
            ),
            TierModel(1, "호야초밥참치 본점", "일식", "건입~중문",  "", -1, "",
                isFavorite = true,
                isChecked = true
            ),
            TierModel(1, "호야초밥참치 본점", "일식", "건입~중문",  "", -1, "",
                isFavorite = true,
                isChecked = true
            ),
            TierModel(1, "호야초밥참치 본점", "일식", "건입~중문",  "", -1, "",
                isFavorite = true,
                isChecked = true
            ),
            TierModel(1, "호야초밥참치 본점", "일식", "건입~중문",  "", -1, "",
                isFavorite = true,
                isChecked = true
            ),
            TierModel(1, "호야초밥참치 본점", "일식", "건입~중문",  "", -1, "",
                isFavorite = true,
                isChecked = true
            ),
            TierModel(1, "호야초밥참치 본점", "일식", "건입~중문",  "", -1, "",
                isFavorite = true,
                isChecked = true
            ),
            TierModel(1, "호야초밥참치 본점", "일식", "건입~중문",  "", -1, "",
                isFavorite = true,
                isChecked = true
            ),
            TierModel(1, "호야초밥참치 본점", "일식", "건입~중문",  "", -1, "",
                isFavorite = true,
                isChecked = true
            ),
            TierModel(1, "호야초밥참치 본점", "일식", "건입~중문",  "", -1, "",
                isFavorite = true,
                isChecked = true
            ),
            TierModel(1, "호야초밥참치 본점", "일식", "건입~중문",  "", -1, "",
                isFavorite = true,
                isChecked = true
            )
            // Add more data here
        )
        tierAdapter.submitList(exampleData)
    }
}