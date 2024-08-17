package com.kust.kustaurant.presentation.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kust.kustaurant.databinding.FragmentDetailMenuBinding

class DetailMenuFragment : Fragment() {
    lateinit var binding: FragmentDetailMenuBinding
    private lateinit var menuAdapter: DetailMenuAdapter
    private var menuList: ArrayList<MenuData> = arrayListOf()
    private val viewModel: DetailViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailMenuBinding.inflate(layoutInflater)
        binding.lifecycleOwner = requireActivity()
        binding.viewModel = viewModel

        observeViewModel()
        Log.d("this3.1", viewModel.menuData.value.toString())
        initMenuRecyclerView()
        Log.d("this3.2", viewModel.menuData.value.toString())

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setRecyclerViewHeight() // 프래그먼트가 다시 보여질 때 마다 높이 재설정
    }

    private fun observeViewModel() {
        viewModel.menuData.observe(requireActivity()){ menuData ->
            menuAdapter.submitList(menuData)
            setRecyclerViewHeight()
        }
    }

    private fun initMenuRecyclerView() {
        menuAdapter = DetailMenuAdapter(requireContext())
        binding.rvMenu.adapter = menuAdapter
        Log.d("this9", viewModel.menuData.value.toString())
        binding.rvMenu.layoutManager = object : LinearLayoutManager(requireContext()) {
            override fun canScrollVertically(): Boolean {
                return false // 스크롤 비활성화
            }
        }
    }

    private fun setRecyclerViewHeight() {
        binding.rvMenu.post {
            var totalHeight = 0
            val measuredWidth = binding.rvMenu.measuredWidth
            for (i in 0 until menuAdapter.itemCount) {
                val childView = menuAdapter.createViewHolder(binding.rvMenu, menuAdapter.getItemViewType(i)).itemView
                childView.measure(
                    View.MeasureSpec.makeMeasureSpec(measuredWidth, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                )
                totalHeight += childView.measuredHeight + 40
            }
            val params = binding.rvMenu.layoutParams
            params.height = totalHeight
            binding.rvMenu.layoutParams = params

            // ViewPager 높이 조정을 위한 메소드 호출
            if (activity is DetailActivity) {
                (activity as DetailActivity).setViewPagerHeight(totalHeight)
            }
        }
    }
}
