package com.kust.kustaurant.presentation.ui.tier

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kust.kustaurant.R
import com.kust.kustaurant.databinding.FragmentTierListBinding
import com.kust.kustaurant.domain.model.TierRestaurant
import com.kust.kustaurant.presentation.ui.detail.DetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TierListFragment : Fragment() {
    private lateinit var binding: FragmentTierListBinding
    private val viewModel: TierViewModel by activityViewModels()
    private lateinit var tierAdapter: TierListAdapter
    private var recyclerViewState: Parcelable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTierListBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@TierListFragment.viewModel
        }

        initializeFilters()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedInstanceState?.let {
            recyclerViewState = it.getParcelable("recyclerViewState")
        }

        setupRecyclerView()
        setupRefreshListener()
        setupScrollListener()
        setupObservers()

        // Restore RecyclerView state
        recyclerViewState?.let { state ->
            binding.tierRecyclerView.layoutManager?.onRestoreInstanceState(state)
        }
    }

    private fun initializeFilters() {
        if (viewModel.selectedMenus.value == setOf("") &&
            viewModel.selectedSituations.value == setOf("") &&
            viewModel.selectedLocations.value == setOf("")
        ) {
            viewModel.setCategory(setOf("전체"), setOf("전체"), setOf("전체"))
        }
    }

    private fun setupRecyclerView() {
        tierAdapter = TierListAdapter(requireContext()).apply {
            setOnItemClickListener(object : TierListAdapter.OnItemClickListener {
                override fun onItemClicked(data: TierRestaurant) {
                    val intent = Intent(requireActivity(), DetailActivity::class.java).apply {
                        putExtra("restaurantId", data.restaurantId)
                    }
                    val options = ActivityOptions.makeCustomAnimation(
                        requireContext(), R.anim.slide_in_right, R.anim.stay_in_place
                    )
                    startActivity(intent, options.toBundle())
                }
            })
        }

        binding.tierRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = tierAdapter
        }
    }

    private fun setupRefreshListener() {
        binding.tierSrl.setOnRefreshListener {
            tierAdapter.submitList(emptyList())
            viewModel.fetchFirstRestaurants()
            binding.tierSrl.isRefreshing = false
        }
    }

    private fun setupScrollListener() {
        binding.tierRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) {
                    viewModel.fetchNextRestaurants()
                }
            }
        })
    }

    private fun setupObservers() {
        viewModel.categoryChangeList.observe(viewLifecycleOwner) { changed ->
            if (changed && recyclerViewState == null) {
                tierAdapter.submitList(emptyList())
                viewModel.fetchFirstRestaurants()
            }
        }

        viewModel.fetchedRestaurants.observe(viewLifecycleOwner) { tierList ->
            val currentList = tierAdapter.getCurrentList().toMutableList()
            currentList.addAll(tierList)
            tierAdapter.submitList(currentList)
        }

        viewModel.isExpanded.observe(viewLifecycleOwner) { isExpanded ->
            tierAdapter.setExpanded(isExpanded)
            val text =
                if (isExpanded) getString(R.string.small_btn_info) else getString(R.string.wide_btn_info)
            val content = SpannableString(text).apply {
                setSpan(UnderlineSpan(), 0, text.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            }
            binding.tierTvToggleExtendReduce.text = content
        }
    }

    override fun onResume() {
        super.onResume()
        val lastPosition = viewModel.getTierListLastPosition()
        binding.tierRecyclerView.visibility = View.INVISIBLE

        viewModel.allRestaurants.value?.let { restaurants ->
            tierAdapter.submitList(restaurants) {
                binding.tierRecyclerView.post {
                    (binding.tierRecyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
                        lastPosition,
                        0
                    )
                    binding.tierRecyclerView.postDelayed({
                        binding.tierRecyclerView.visibility = View.VISIBLE
                    }, 50)
                }
            }
        }
    }


    override fun onStop() {
        super.onStop()
        viewModel.setTierListLastPosition(
            (binding.tierRecyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(
            "recyclerViewState",
            binding.tierRecyclerView.layoutManager?.onSaveInstanceState()
        )
    }
}