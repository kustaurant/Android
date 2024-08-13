package com.example.kustaurant.presentation.ui.tier

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kustaurant.databinding.FragmentTierListBinding
import com.example.kustaurant.domain.model.TierRestaurant
import com.example.kustaurant.presentation.ui.detail.DetailActivity
import com.example.kustaurant.presentation.ui.splash.StartActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TierListFragment : Fragment() {
    private lateinit var binding: FragmentTierListBinding
    private val viewModel: TierViewModel by activityViewModels()
    private lateinit var tierAdapter: TierListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTierListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        setupRecyclerView()

        binding.tierSrl.setOnRefreshListener {
            viewModel.checkAndLoadBackendData(0)
            binding.tierRecyclerView.scrollToPosition(0)
            binding.tierSrl.isRefreshing = false
        }

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
            tierAdapter.submitList(tierList)
        }

        viewModel.isExpanded.observe(viewLifecycleOwner) { isExpanded ->
            tierAdapter.setExpanded(isExpanded)
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.checkAndLoadBackendData(0)
    }
}