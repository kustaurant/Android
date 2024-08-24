package com.kust.kustaurant.presentation.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kust.kustaurant.R
import com.kust.kustaurant.data.model.CommentDataResponse
import com.kust.kustaurant.databinding.FragmentDetailReviewBinding

class DetailReviewFragment : Fragment() {
    lateinit var binding : FragmentDetailReviewBinding
    lateinit var reviewAdapter: DetailReviewAdapter
    private val viewModel: DetailViewModel by activityViewModels()
    private var restaurantId = 0
    private val popularity = "popularity"
    private val latest = "latest"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailReviewBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        if (arguments != null) {
//            "애드앤드 테스트 아이디 741"
            restaurantId = requireArguments().getInt("restaurantId", 0);
            Log.d("restaurantId", restaurantId.toString())

            viewModel.loadCommentData(restaurantId, popularity)
            updateButton(binding.detailBtnPopular)
        }

        observeViewModel()
        initRecyclerView()

        binding.detailBtnRecent.setOnClickListener {
            updateButton(it)
            viewModel.loadCommentData(restaurantId, latest)
        }

        binding.detailBtnPopular.setOnClickListener {
            updateButton(it)
            viewModel.loadCommentData(restaurantId, popularity)
        }

        return binding.root
    }

    private fun updateButton(selectedView: View?) {
        val views = listOf(binding.detailBtnRecent, binding.detailBtnPopular)
        val textViews = listOf(binding.detailTvRecent, binding.detailTvPopular)

        for (index in views.indices) {
            val view = views[index]
            val textView = textViews[index]
            val isSelected = view == selectedView

            view.isSelected = isSelected
            textView.isSelected = isSelected
        }
    }

    private fun observeViewModel() {
        viewModel.reviewData.observe(viewLifecycleOwner){ commentData ->
            if(commentData.isEmpty()){
                binding.detailBtnRecent.visibility = View.GONE
                binding.detailBtnPopular.visibility = View.GONE
                binding.detailClReviewNone.visibility = View.VISIBLE
            }
            reviewAdapter.submitList(commentData.toList())
            reviewAdapter.notifyDataSetChanged()
            Log.d("commentData", commentData.toString())
            setRecyclerViewHeight()
        }
    }

    override fun onResume() {
        super.onResume()
        setRecyclerViewHeight() // 프래그먼트가 다시 보여질 때 마다 높이 재설정

    }

    private fun initRecyclerView() {
        reviewAdapter = DetailReviewAdapter(requireContext())

        binding.detailRvReview.adapter = reviewAdapter
        binding.detailRvReview.layoutManager = LinearLayoutManager(requireContext())

        reviewAdapter.setOnItemClickListener(object : DetailReviewAdapter.OnItemClickListener {
            override fun onReportClicked(commentId: Int) {
                val intent = Intent(context, ReportActivity::class.java)
                intent.putExtra("commentId", commentId)
                startActivity(intent)
            }

            override fun onDeleteClicked(commentId: Int) {

            }

            override fun onCommentClicked(commentId: Int) {
                showBottomSheetInput(commentId)
            }

        })
    }

    private fun showBottomSheetInput(commentId: Int) {
        val bottomSheetDialog = BottomSheetDialog(requireContext()).apply {
            setCancelable(true)
            setCanceledOnTouchOutside(true)
        }
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_comment, null)
        bottomSheetDialog.setContentView(bottomSheetView)

        val etInput = bottomSheetView.findViewById<EditText>(R.id.detail_et_input)
        val btnSubmit = bottomSheetView.findViewById<ConstraintLayout>(R.id.detail_cl_comment_confirm)

        bottomSheetDialog.setOnShowListener {
            etInput.requestFocus()
            // 바텀 sheet 생성하는데 시간 지연
            etInput.postDelayed({
                val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.showSoftInput(etInput, InputMethodManager.SHOW_IMPLICIT)
            }, 100)
        }

        btnSubmit.setOnClickListener {
            val inputText = etInput.text.toString()
            if (inputText.isNotBlank()) {
                viewModel.postCommentData(restaurantId, commentId, inputText)
                bottomSheetDialog.dismiss()
                Toast.makeText(requireContext(), " $inputText", Toast.LENGTH_SHORT).show()
            } else {
                etInput.error = "Please enter a comment"
                Toast.makeText(requireContext(), "텍스트를 입력해주세요", Toast.LENGTH_SHORT).show()
            }
        }

        bottomSheetDialog.setOnDismissListener {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(etInput.windowToken, 0)
        }

        bottomSheetDialog.show()
    }

    // 각 tab마다 height 재구성
    private fun setRecyclerViewHeight() {
        var totalHeight = 0
        val layoutManager = binding.detailRvReview.layoutManager as LinearLayoutManager

        for (i in 0 until reviewAdapter.itemCount) {
            val holder = reviewAdapter.createViewHolder(binding.detailRvReview, reviewAdapter.getItemViewType(i))
            reviewAdapter.onBindViewHolder(holder, i)

            val innerRecyclerView = holder.itemView.findViewById<RecyclerView>(R.id.detail_rv_reply)
            val innerHeight = calInnerHeight(innerRecyclerView)

            holder.itemView.measure(
                View.MeasureSpec.makeMeasureSpec(binding.detailRvReview.width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )

            // ViewHolder의 마진 및 패딩
//            val lp = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
            totalHeight += holder.itemView.measuredHeight + innerHeight
        }

        val params = binding.detailRvReview.layoutParams
        params.height = totalHeight
        binding.detailRvReview.layoutParams = params

        // DetailActivity에서 상속
        if (activity is DetailActivity) {
            (activity as DetailActivity).setViewPagerHeight(totalHeight)
        }
    }

    private fun calInnerHeight(recyclerView: RecyclerView): Int {
        var innerHeight = 0
        val innerAdapter = recyclerView.adapter ?: return 0
        for (j in 0 until innerAdapter.itemCount) {
            val type = innerAdapter.getItemViewType(j)
            val innerHolder = innerAdapter.createViewHolder(recyclerView, type)
            innerAdapter.onBindViewHolder(innerHolder, j)
            innerHolder.itemView.measure(
                View.MeasureSpec.makeMeasureSpec(recyclerView.width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )
            innerHeight += innerHolder.itemView.measuredHeight
        }
        return innerHeight
    }

}