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
import com.kust.kustaurant.data.getAccessToken
import com.kust.kustaurant.data.model.CommentDataResponse
import com.kust.kustaurant.databinding.FragmentDetailReviewBinding
import com.kust.kustaurant.presentation.ui.splash.StartActivity

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

    fun checkToken(action: () -> Unit) {
        val accessToken = getAccessToken(requireContext())
        if (accessToken == null) {
            val intent = Intent(context, StartActivity::class.java)
            startActivity(intent)
        } else {
            action()
        }
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
        viewModel.loadCommentData(restaurantId, popularity)
        setRecyclerViewHeight() // 프래그먼트가 다시 보여질 때 마다 높이 재설정

    }

    private fun initRecyclerView() {
        reviewAdapter = DetailReviewAdapter(requireContext())

        binding.detailRvReview.adapter = reviewAdapter
        binding.detailRvReview.layoutManager = LinearLayoutManager(requireContext())

        reviewAdapter.setOnItemClickListener(object : DetailReviewAdapter.OnItemClickListener {
            override fun onReportClicked(commentId: Int) {
                checkToken{
                    val intent = Intent(context, ReportActivity::class.java)
                    intent.putExtra("commentId", commentId)
                    startActivity(intent)
                }
            }

            override fun onDeleteClicked(commentId: Int) {
                checkToken{
                    viewModel.deleteCommentData(restaurantId, commentId)
                    Toast.makeText(requireContext(), "댓글 삭제가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    viewModel.loadCommentData(restaurantId, popularity)
                }
            }

            override fun onCommentClicked(commentId: Int) {
                checkToken{
                    showBottomSheetInput(commentId)
                }
            }

            override fun onLikeClicked(commentId: Int) {
                checkToken{
                    viewModel.postCommentLike(restaurantId, commentId)
                    // 여기에 lottie animation?
                    viewModel.loadCommentData(restaurantId, popularity)
                }
            }

            override fun onDisLikeClicked(commentId: Int) {
                checkToken{
                    viewModel.postCommentDisLike(restaurantId, commentId)
                    viewModel.loadCommentData(restaurantId, popularity)
                }
            }
        })

        reviewAdapter.interactionListener = object : DetailRelyAdapter.OnItemClickListener{
            override fun onReportClicked(commentId: Int) {
                checkToken{
                    val intent = Intent(context, ReportActivity::class.java)
                    intent.putExtra("commentId", commentId)
                    startActivity(intent)
                }
            }

            override fun onDeleteClicked(commentId: Int) {
                checkToken{
                    viewModel.deleteCommentData(restaurantId, commentId)
                    Toast.makeText(requireContext(), "댓글 삭제가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    viewModel.loadCommentData(restaurantId, popularity)
                }
            }

            override fun onLikeClicked(commentId: Int) {
                checkToken{
                    viewModel.postCommentLike(restaurantId, commentId)
                    viewModel.loadCommentData(restaurantId, popularity)
                }
            }

            override fun onDisLikeClicked(commentId: Int) {
                checkToken{
                    viewModel.postCommentDisLike(restaurantId, commentId)
                    viewModel.loadCommentData(restaurantId, popularity)
                }
            }
        }
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
                Toast.makeText(requireContext(), "대댓글이 등록되었습니다.", Toast.LENGTH_SHORT).show()
                viewModel.loadCommentData(restaurantId, popularity)
                setRecyclerViewHeight()
            } else {
                etInput.error = "텍스트를 입력해주세요"
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
        val buttonHeight = binding.detailBtnPopular.height + binding.detailBtnRecent.height

        binding.detailRvReview.post {
            var totalHeight = 0
            val layoutManager = binding.detailRvReview.layoutManager as LinearLayoutManager
            for (i in 0 until reviewAdapter.itemCount) {
                val childView = layoutManager.findViewByPosition(i) ?: continue // 이미 렌더링된 뷰 사용
                val lp = childView.layoutParams as ViewGroup.MarginLayoutParams

                childView.measure(
                    View.MeasureSpec.makeMeasureSpec(binding.detailRvReview.width - lp.leftMargin - lp.rightMargin, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                )

                totalHeight += childView.measuredHeight + lp.topMargin + lp.bottomMargin
            }
            totalHeight += buttonHeight

            val params = binding.detailRvReview.layoutParams
            params.height = totalHeight
            binding.detailRvReview.layoutParams = params

            // ViewPager 높이 조정을 위한 메소드 호출
            (activity as? DetailActivity)?.setViewPagerHeight(totalHeight)
        }
    }

}