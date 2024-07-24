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
        setupCategoryButton()
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
        binding.recyclerviewTier.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && binding.fabExpand.isShown) {
                    // 아래로 스크롤: FAB 숨기기
                    binding.fabExpand.hide()
                } else if (dy < 0 && !binding.fabExpand.isShown) {
                    // 위로 스크롤: FAB 보이기
                    binding.fabExpand.show()
                }
            }
        })
    }

    private fun setupCategoryButton() {
        binding.btnCategory.setOnClickListener {
            val fragment = TierCategoryFragment().apply {
                arguments = Bundle().apply {
                    putInt("fromTabIndex", 0) // Assuming 0 is the index for the list tab
                }
            }
            (requireParentFragment() as? TierFragment)?.let {
                it.binding.viewPager.currentItem = 0 // Index of TierCategoryFragment in the ViewPager
            }
        }
    }


}


