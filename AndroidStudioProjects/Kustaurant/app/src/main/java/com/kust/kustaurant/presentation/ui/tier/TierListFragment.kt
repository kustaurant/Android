package com.kust.kustaurant.presentation.ui.tier

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kust.kustaurant.databinding.FragmentTierListBinding
import com.kust.kustaurant.domain.model.TierRestaurant
import com.kust.kustaurant.presentation.ui.detail.DetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TierListFragment : Fragment() {
    private lateinit var binding: FragmentTierListBinding
    private val viewModel: TierViewModel by activityViewModels()
    private lateinit var tierAdapter: TierListAdapter
    private val allTierData = mutableListOf<TierRestaurant>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTierListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        setupRecyclerView()

        binding.tierSrl.setOnRefreshListener {
            refreshData()
        }

        binding.tierRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // 스크롤이 끝에 도달했는지 확인
                if (!recyclerView.canScrollVertically(1)) {
                    // 최하단에서 다시 스크롤할 때 호출할 코드
                    viewModel.applyFilters(
                        viewModel.selectedMenus.value ?: emptySet(),
                        viewModel.selectedSituations.value ?: emptySet(),
                        viewModel.selectedLocations.value ?: emptySet(),
                        0
                    )
                }
            }
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
    }

    private fun setupRecyclerView() {
        tierAdapter = TierListAdapter()
        binding.tierRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = tierAdapter
        }

        tierAdapter.setOnItemClickListener(object : TierListAdapter.OnItemClickListener{
            override fun onItemClicked(data: TierRestaurant) {
                val intent = Intent(requireActivity(), DetailActivity::class.java)
                intent.putExtra("restaurantId", data.restaurantId)
                startActivity(intent)
            }
        })
    }

    private fun observeViewModel() {
        viewModel.tierRestaurantList.observe(viewLifecycleOwner) { tierList ->

            if(viewModel.isSelectedCategoriesChanged.value == true) {
                allTierData.clear()
                binding.tierRecyclerView.scrollToPosition(0)
                Log.e("TIerList", "들옴")
            }

            Log.e("TierFragment", viewModel.isSelectedCategoriesChanged.toString())

            allTierData.addAll(tierList)
            tierAdapter.submitList(allTierData.toList())
        }

        viewModel.isExpanded.observe(viewLifecycleOwner) { isExpanded ->
            tierAdapter.setExpanded(isExpanded)
        }
    }

    private fun refreshData() {
        allTierData.clear()
        viewModel.checkAndLoadBackendListData(TierViewModel.Companion.RestaurantState.RELOAD_RESTAURANT_LIST_DATA)
        binding.tierRecyclerView.scrollToPosition(0)
        binding.tierSrl.isRefreshing = false
    }

    override fun onResume() {
        super.onResume()
        Log.e("TierFragment", "onResume is called")

        binding.tierRecyclerView.scrollToPosition(0)
        //viewModel.checkAndLoadBackendListData(TierViewModel.Companion.RestaurantState.NEXT_PAGE_LIST_DATA)
    }
}