package com.example.kustaurant.presentation.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kustaurant.R
import com.example.kustaurant.databinding.FragmentDetailMenuBinding

class DetailMenuFragment : Fragment() {
    lateinit var binding: FragmentDetailMenuBinding
    lateinit var menuAdapter: DetailMenuAdapter
    private var menuList: ArrayList<MenuData> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailMenuBinding.inflate(layoutInflater)

        initDummyData()
        initMenuRecyclerView()

        return binding.root
    }

    private fun initDummyData() {
        menuList.addAll(
            arrayListOf(
                MenuData("", "A특호야", 1000),
                MenuData("", "B특호야", 2000),
                MenuData("", "C특호야", 3000),
                MenuData("", "D특호야", 4000),
                MenuData("", "E특호야", 5000),
                MenuData("", "F특호야", 6000),
                MenuData("", "G특호야", 7000),
                MenuData("", "H특호야", 8000),
                MenuData("", "I특호야", 9000),
                MenuData("", "J특호야", 10000),
                MenuData("", "K특호야", 11000),
                MenuData("", "L특호야", 12000)
            )
        )
    }

    private fun initMenuRecyclerView() {
        menuAdapter = DetailMenuAdapter(requireContext(), menuList)
        binding.rvMenu.adapter = menuAdapter
        binding.rvMenu.layoutManager = object : LinearLayoutManager(requireContext()) {
            override fun canScrollVertically(): Boolean {
                return false // 스크롤 비활성화
            }
        }
        setRecyclerViewHeight()
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
                totalHeight += childView.measuredHeight
            }
            val params = binding.rvMenu.layoutParams
            params.height = totalHeight
            binding.rvMenu.layoutParams = params
        }
    }
}
