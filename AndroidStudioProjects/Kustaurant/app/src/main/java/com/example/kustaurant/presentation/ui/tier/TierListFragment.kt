package com.example.kustaurant.presentation.ui.tier

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kustaurant.TierListAdapter
import com.example.kustaurant.databinding.FragmentTierListBinding
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
        setupScrollListener()
    }

    private fun setupRecyclerView() {
        tierAdapter = TierListAdapter()
        binding.recyclerviewTier.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = tierAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.tierList.observe(viewLifecycleOwner) { tierList ->
            tierAdapter.submitList(tierList)
        }

        viewModel.isExpanded.observe(viewLifecycleOwner) { isExpanded ->
            tierAdapter.setExpanded(isExpanded)
        }
    }

    private fun setupScrollListener() {
        var isHiding = false
        binding.recyclerviewTier.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && binding.fabExpand.isShown && !isHiding) {
                    // 아래로 스크롤: FAB 숨기기
                    isHiding = true
                    binding.fabExpand.animate()
                        .scaleX(0f)
                        .scaleY(0f)
                        .setDuration(200)
                        .withEndAction {
                            binding.fabExpand.visibility = View.GONE
                            isHiding = false
                        }
                        .start()
                } else if (dy < 0 && !binding.fabExpand.isShown) {
                    // 위로 스크롤: FAB 보이기
                    binding.fabExpand.visibility = View.VISIBLE
                    binding.fabExpand.scaleX = 0f
                    binding.fabExpand.scaleY = 0f
                    binding.fabExpand.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(200)
                        .start()
                }
            }
        })
    }
}