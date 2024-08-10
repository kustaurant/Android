package com.example.kustaurant.presentation.ui.detail

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kustaurant.MainActivity
import com.example.kustaurant.R
import com.example.kustaurant.databinding.DialogReviewBinding
import com.example.kustaurant.databinding.FragmentDetailReviewBinding

class DetailReviewFragment : Fragment() {
    lateinit var binding : FragmentDetailReviewBinding
    lateinit var reviewAdapter: DetailReviewAdapter
    private var reviewList : ArrayList<ReviewData> = arrayListOf()
    private var replyList : ArrayList<ReviewReplyData> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailReviewBinding.inflate(layoutInflater)

        initDummyData()
        initRecyclerView()

        return binding.root
    }

    private fun initDummyData() {
        replyList.addAll(
            arrayListOf(
                ReviewReplyData("대댓글1", "20초전","아닌데?", 12, 13),
                ReviewReplyData("대댓글2", "20초전","아닌데?", 12, 13)
            )
        )
        reviewList.addAll(
            arrayListOf(
                ReviewData(4.0, "리뷰1", "30초전", "개존맛", 122, 123, replyList),
                ReviewData(4.0, "리뷰2", "30초전", "개존맛", 122, 123, replyList),
                ReviewData(4.0, "리뷰3", "30초전", "개존맛", 122, 123, replyList),
                ReviewData(4.0, "리뷰4", "30초전", "개존맛", 122, 123, replyList),
                ReviewData(4.0, "리뷰5", "30초전", "개존맛", 122, 123, replyList),
                ReviewData(4.0, "리뷰6", "30초전", "개존맛", 122, 123, replyList),
                ReviewData(4.0, "리뷰7", "30초전", "개존맛", 122, 123, replyList),
                ReviewData(4.0, "리뷰8", "30초전", "개존맛", 122, 123, replyList),
                ReviewData(4.0, "리뷰9", "30초전", "개존맛", 122, 123, replyList),
                ReviewData(4.0, "리뷰10", "30초전", "개존맛", 122, 123, replyList),
                ReviewData(4.0, "리뷰11", "30초전", "개존맛", 122, 123, replyList),
                ReviewData(4.0, "리뷰12", "30초전", "개존맛", 122, 123, replyList),

            )
        )
    }

    private fun initRecyclerView() {
        reviewAdapter = DetailReviewAdapter(reviewList){
            updateRecyclerViewHeight()
        }
        binding.detailRvReview.addItemDecoration(ItemDecoration(spacing = 16.dpToPx(requireContext()), endSpacing = 32.dpToPx(requireContext())))
        binding.detailRvReview.adapter = reviewAdapter
        val layoutManager = object : LinearLayoutManager(context) {
            override fun canScrollVertically(): Boolean = false // 스크롤 비활성화
        }
        binding.detailRvReview.layoutManager = layoutManager
        binding.detailRvReview.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            updateRecyclerViewHeight()
        }

        reviewAdapter.setOnItemClickListener(object : DetailReviewAdapter.OnItemClickListener {
            override fun onItemClicked(data: ReviewData, position : Int, type : Int) {
                when (type) {
                    1 -> {  // Report
                        context?.let {
                            val intent = Intent(it, ReportActivity::class.java)
                            startActivity(intent)
                        }
                    }
                    2 -> {  // Delete
                        if (position < reviewList.size) {
                            reviewList.removeAt(position)
                            reviewAdapter.notifyItemRemoved(position)
                            reviewAdapter.notifyItemRangeChanged(position, reviewList.size - position)
                        }
                    }
                }
            }
        })
    }

    private fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }

    // 각 tab마다 height 재구성
    private fun updateRecyclerViewHeight() {
        var totalHeight = 0
        for (i in 0 until reviewAdapter.itemCount) {
            val holder = reviewAdapter.createViewHolder(binding.detailRvReview, reviewAdapter.getItemViewType(i))
            reviewAdapter.onBindViewHolder(holder, i)
            holder.itemView.measure(
                View.MeasureSpec.makeMeasureSpec(binding.detailRvReview.width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )
            totalHeight += holder.itemView.measuredHeight
        }
        val params = binding.detailRvReview.layoutParams
        params.height = totalHeight
        binding.detailRvReview.layoutParams = params

        // DetailActivity에서 상속
        if (activity is DetailActivity) {
            (activity as DetailActivity).setViewPagerHeight(binding.detailRvReview.measuredHeight)
        }
    }
}