package com.example.kustaurant.presentation.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kustaurant.R
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
                ReviewReplyData("역병2", "20초전","아닌데?", 12, 13),
                ReviewReplyData("역병3", "20초전","아닌데?", 12, 13)
            )
        )
        reviewList.addAll(
            arrayListOf(
                ReviewData(4.0, "역병", "30초전", "개존맛", 122, 123, replyList),
                ReviewData(4.0, "역병", "30초전", "개존맛", 122, 123, replyList),
                ReviewData(4.0, "역병", "30초전", "개존맛", 122, 123, replyList),
                ReviewData(4.0, "역병", "30초전", "개존맛", 122, 123, replyList),
                ReviewData(4.0, "역병", "30초전", "개존맛", 122, 123, replyList)
            )
        )
    }

    private fun initRecyclerView() {
        reviewAdapter = DetailReviewAdapter(reviewList)
        binding.detailRvReview.adapter = reviewAdapter
        binding.detailRvReview.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }
}