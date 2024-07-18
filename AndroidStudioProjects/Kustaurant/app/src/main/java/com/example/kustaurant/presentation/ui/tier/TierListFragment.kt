package com.example.kustaurant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kustaurant.databinding.FragmentTierListBinding

class TierListFragment : Fragment() {

    private lateinit var binding: FragmentTierListBinding
    private lateinit var scrollListener: RecyclerView.OnScrollListener
    private lateinit var tierAdapter: TierListAdapter
    private var isExpanded = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTierListBinding.inflate(inflater, container, false)
        setupRecyclerView()
        setupFAB()
        setupScrollListener()
        return binding.root
    }

    private fun setupRecyclerView() {
        tierAdapter = TierListAdapter(isExpanded)
        binding.recyclerviewTier.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = tierAdapter
        }

        // 예제 데이터 추가
        val exampleData = listOf(
            TierModel(1, "호야초밥참치 본점", "일식", "건입~중문", "", -1, "",
                isFavorite = true,
                isChecked = true, "어디대과 10% 할인"
            ),
            TierModel(134, "호야초밥참치 본점", "일식", "건입~중문",  "", 1, "",
                isFavorite = true,
                isChecked = true, "어디대과 10% 할인"
            ),
            TierModel(145, "호야초밥참치 본점", "일식", "건입~중문",  "", -1, "",
                isFavorite = true,
                isChecked = true, ""
            ),
            TierModel(156, "호야초밥참치 본234235235235점", "일식", "건입~중문",  "", 3, "",
                isFavorite = true,
                isChecked = true, "어디대과 10% 할인"
            ),
            TierModel(1, "호야초밥참치 본점", "일식", "건입~중문",  "", -1, "",
                isFavorite = true,
                isChecked = true, ""
            ),
            TierModel(1, "호야초밥참치 본점", "일식", "건입~중문",  "", -2, "",
                isFavorite = true,
                isChecked = true, ""
            ),
            TierModel(1, "호야초밥참치 본점", "일식", "건입~중문",  "", -1, "",
                isFavorite = true,
                isChecked = true, ""
            ),
            TierModel(13, "호야초밥참치 본점", "일식", "건입~중문",  "", 1, "",
                isFavorite = true,
                isChecked = true, "어디대과 10% 할인"
            ),
            TierModel(1, "호야초밥참치 본점", "일식", "건입~중문",  "", 2, "",
                isFavorite = true,
                isChecked = true, "어디대과 10% 할인"
            ),
            TierModel(1, "호야초밥참치 본점", "일식", "건입~중문",  "", -1, "",
                isFavorite = true,
                isChecked = true, "어디대과 10% 할인"
            ),
            TierModel(1, "호야초밥참치 본점", "일식", "건입~중문",  "", -1, "",
                isFavorite = true,
                isChecked = true, "어디대과 10% 할인"
            )
            // Add more data here
        )
        tierAdapter.submitList(exampleData)
    }
    private fun setupFAB() {
        binding.fabExpand.setOnClickListener {
            isExpanded = !isExpanded
            updateRecyclerViewLayout()
            updateFABIcon()
        }
    }

    private fun updateRecyclerViewLayout() {
        tierAdapter.setExpanded(isExpanded)
        tierAdapter.notifyDataSetChanged()
    }

    private fun updateFABIcon() {
        binding.fabExpand.setImageResource(
            if (isExpanded) R.drawable.ic_reduce else R.drawable.ic_expand
        )
    }

    private fun setupScrollListener() {
        scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && binding.fabExpand.isShown) {
                    // 스크롤 다운: FAB 숨기기
                    binding.fabExpand.hide()
                } else if (dy < 0 && !binding.fabExpand.isShown) {
                    // 스크롤 업: FAB 보이기
                    binding.fabExpand.show()
                }
            }
        }
        binding.recyclerviewTier.addOnScrollListener(scrollListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerviewTier.removeOnScrollListener(scrollListener)
    }
}