package com.example.kustaurant.presentation.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kustaurant.R
import com.example.kustaurant.databinding.FragmentDetailMenuBinding

class DetailMenuFragment : Fragment() {
    lateinit var binding: FragmentDetailMenuBinding
    lateinit var menuAdapter: DetailMenuAdapter
    private var menuList: ArrayList<MenuData> = arrayListOf()
    private val viewModel: DetailViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailMenuBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        observeViewModel()
        initMenuRecyclerView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerViewHeight() // 최초 로딩시에도 높이 설정
    }

    override fun onResume() {
        super.onResume()
        setRecyclerViewHeight() // 프래그먼트가 다시 보여질 때 마다 높이 재설정
    }

    private fun observeViewModel() {
        viewModel.menuData.observe(viewLifecycleOwner){ menuData ->
            menuAdapter.submitList(menuData)
        }
    }

    private fun initMenuRecyclerView() {
        menuAdapter = DetailMenuAdapter(requireContext())
        binding.rvMenu.adapter = menuAdapter
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
                totalHeight += childView.measuredHeight + 60
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
