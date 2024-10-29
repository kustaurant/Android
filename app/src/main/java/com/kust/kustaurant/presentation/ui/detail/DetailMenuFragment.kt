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

    override fun onStart() {
        super.onStart()
        Log.d("life","menu start")
    }

    override fun onResume() {
        super.onResume()
        Log.d("life","menu resume")
        setRecyclerViewHeight() // 프래그먼트가 다시 보여질 때 마다 높이 재설정
    }

    private fun observeViewModel() {
        viewModel.menuData.observe(viewLifecycleOwner){ menuData ->
            menuAdapter.submitList(menuData)
            setRecyclerViewHeight()
        }
    }

    private fun initMenuRecyclerView() {
        menuAdapter = DetailMenuAdapter(requireContext())
        binding.rvMenu.adapter = menuAdapter
        binding.rvMenu.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setRecyclerViewHeight() {
        binding.rvMenu.post {
            var totalHeight = 0
            val layoutManager = binding.rvMenu.layoutManager as LinearLayoutManager
            for (i in 0 until menuAdapter.itemCount) {
                val childView = layoutManager.findViewByPosition(i) ?: continue // 이미 렌더링된 뷰 사용
                val lp = childView.layoutParams as ViewGroup.MarginLayoutParams

                childView.measure(
                    View.MeasureSpec.makeMeasureSpec(binding.rvMenu.width - lp.leftMargin - lp.rightMargin, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                )

                totalHeight += childView.measuredHeight + lp.topMargin + lp.bottomMargin
            }
            val params = binding.rvMenu.layoutParams
            params.height = totalHeight
            binding.rvMenu.layoutParams = params

            // ViewPager 높이 조정을 위한 메소드 호출
            (activity as? DetailActivity)?.setViewPagerHeight(totalHeight)
        }
    }


}
