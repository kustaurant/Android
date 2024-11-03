package com.kust.kustaurant.presentation.ui.community

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import android.content.Context
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.PopupWindow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kust.kustaurant.MainActivity
import com.kust.kustaurant.R
import com.kust.kustaurant.data.getAccessToken
import com.kust.kustaurant.databinding.FragmentCommunityPostDetailBinding
import com.kust.kustaurant.presentation.ui.splash.StartActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommunityPostWriteFragment : Fragment() {
    private lateinit var binding: FragmentCommunityPostDetailBinding
    private val viewModel: CommunityPostDetailViewModel by activityViewModels()
    private lateinit var CommuCommentAdapter: CommunityPostDetailCommentAdapter
    private val parentViewModel: CommunityViewModel by activityViewModels()
    private var postId : Int  = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCommunityPostDetailBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        initRecyclerView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButton()
        setupObservers()

        binding.communityLlBtnPostLike.background = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = 100f
            setStroke(1, ContextCompat.getColor(requireContext(), R.color.signature_1))
        }
    }

    private fun setupObservers() {
        parentViewModel.selectedPostDetailId.observe(viewLifecycleOwner) { postId ->
            postId?.let {
                this.postId = postId
                viewModel.loadCommunityPostDetail(it)
            }
        }

        viewModel.communityPostDetail.observe(viewLifecycleOwner) { postDetail ->
            postDetail?.let { post ->
                binding.communityTvActivityPostInfo.text = post.postCategory
                binding.communityTvPostTitle.text = post.postTitle
                binding.communityTvPostUser.text = post.user.userNickname
                binding.communityTvTimeAgo.text = post.timeAgo
                binding.communityTvVisitCnt.text = post.postVisitCount
                binding.communityTvLikeCnt.text = post.likeCount.toString()
                binding.communityTvCommentsCnt.text = post.commentCount.toString()
                binding.communityTvTimeAgo.text = post.timeAgo

                binding.communityTvBtnPostLike.text = post.likeCount.toString()
                binding.communityTvBtnScrapLike.text = post.scrapCount.toString()

                loadPostBodyToWebView(post.postBody)

                Glide.with(this)
                    .load(post.user.rankImg)
                    .into(binding.communityIvUserIcon)

                CommuCommentAdapter.submitList(post.postCommentList)
            }
        }

        viewModel.postScrapInfo.observe(viewLifecycleOwner) { result ->
            binding.communityTvBtnScrapLike.text = result.scrapCount.toString()
        }

        viewModel.postLikeInfo.observe(viewLifecycleOwner) { result ->
            binding.communityTvBtnPostLike.text = result.likeCount.toString()
        }

        viewModel.postDelete.observe(viewLifecycleOwner) {result ->
            if(result) {
                requireActivity().supportFragmentManager.popBackStack()

                viewModel.resetPostDelete()
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadPostBodyToWebView(postBody: String) {
        val webView = binding.communityWvMiddleContent
        webView.settings.javaScriptEnabled = false

        // CSS를 추가하여 WebView 내용이 화면 너비에 맞게 조정되도록 함
        val htmlContent = """
        <html>
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <style>
                body { 
                    font-family: sans-serif; 
                    line-height: 1.5; 
                    word-wrap: break-word; 
                    background: rgba(234, 234, 234, 0.1); /* 10% 투명도의 #EAEAEA(cement_2) */
                }
                img { max-width: 100%; height: auto; }
            </style>
        </head>
        <body>
            $postBody
        </body>
        </html>
    """.trimIndent()

        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
    }

    private fun setupButton() {
        binding.btnBack.setOnClickListener {
            (requireActivity() as? MainActivity)?.let { mainActivity ->
                mainActivity.supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, CommunityPostListFragment())
                    .commit()
                mainActivity.binding.mainNavigation.selectedItemId = R.id.menu_community
            }
        }
        binding.communityLlBtnPostLike.setOnClickListener {
            viewModel.postPostLike(postId)
        }
        binding.communityLlBtnScrap.setOnClickListener {
            viewModel.postPostDetailScrap(postId)
        }

        binding.communityIvPostDetailDots.setOnClickListener { view ->
            if(viewModel.postMine.value!!) {
                showPopupWindow(view)
            }
        }
    }

    private fun showPopupWindow(anchorView: View) {
        val inflater = LayoutInflater.from(context)
        val layoutRes = R.layout.popup_review_only_delete

        val popupView = inflater.inflate(layoutRes, null)
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        popupView.findViewById<ConstraintLayout>(R.id.cl_delete)?.setOnClickListener {
            viewModel.deletePost(postId)
            popupWindow.dismiss()
        }

        popupWindow.showAsDropDown(anchorView)
    }

    private fun initRecyclerView() {
        CommuCommentAdapter = CommunityPostDetailCommentAdapter(requireContext())
        binding.communityRvComment.layoutManager = LinearLayoutManager(requireContext())
        binding.communityRvComment.adapter = CommuCommentAdapter
        binding.communityRvComment.itemAnimator = null

        CommuCommentAdapter.setOnItemClickListener(object :
            CommunityPostDetailCommentAdapter.OnItemClickListener {
            override fun onReportClicked(commentId: Int) {
                checkToken {
                    //viewModel.postCommentReport(restaurantId, commentId)
                    Toast.makeText(requireContext(), "신고가 접수되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onDeleteClicked(commentId: Int) {
                checkToken {
                    viewModel.deleteComment(postId, commentId)
                    Toast.makeText(requireContext(), "댓글 삭제가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCommentClicked(commentId: Int) {
                checkToken {
                    showBottomSheetInput(commentId)
                }
            }

            override fun onLikeClicked(commentId: Int, position: Int) {
                checkToken {
                    viewModel.postCommentReact(commentId, "likes")
                }
            }

            override fun onDisLikeClicked(commentId: Int, position: Int) {
                checkToken {
                    viewModel.postCommentReact(commentId, "dislikes")
                }
            }
        })

        CommuCommentAdapter.interactionListener =
            object : CommunityPostDetailCommentReplyAdapter.OnItemClickListener {
                override fun onReportClicked(commentId: Int) {
                    checkToken {
                        //viewModel.postCommentReport(restaurantId, commentId)
                        Toast.makeText(requireContext(), "신고가 접수되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onDeleteClicked(commentId: Int) {
                    checkToken {
                        viewModel.deleteComment(postId, commentId)
                        Toast.makeText(requireContext(), "댓글 삭제가 완료되었습니다.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onLikeClicked(commentId: Int, position: Int) {
                    checkToken {
                        viewModel.postCommentReact(commentId, "likes")
                    }
                }

                override fun onDisLikeClicked(commentId: Int, position: Int) {
                    checkToken {
                        viewModel.postCommentReact(commentId, "dislikes")
                    }
                }
            }
    }

    fun checkToken(action: () -> Unit) {
        val accessToken = getAccessToken(requireContext())
        if (accessToken == null) {
            val intent = Intent(requireContext(), StartActivity::class.java)
            startActivity(intent)
        } else {
            action()
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
        val btnSubmit =
            bottomSheetView.findViewById<ConstraintLayout>(R.id.detail_cl_comment_confirm)

        bottomSheetDialog.setOnShowListener {
            etInput.requestFocus()
            // 바텀 sheet 생성하는데 시간 지연
            etInput.postDelayed({
                val imm =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.showSoftInput(etInput, InputMethodManager.SHOW_IMPLICIT)
            }, 100)
        }

        btnSubmit.setOnClickListener {
            val inputText = etInput.text.toString()
            if (inputText.isNotBlank()) {
                viewModel.postCreateCommentReply(inputText, parentViewModel.selectedPostDetailId.value.toString(), commentId.toString())
                bottomSheetDialog.dismiss()
                Toast.makeText(requireContext(), "대댓글이 등록되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                etInput.error = "텍스트를 입력해주세요"
                Toast.makeText(requireContext(), "텍스트를 입력해주세요", Toast.LENGTH_SHORT).show()
            }
        }

        bottomSheetDialog.setOnDismissListener {
            val imm =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(etInput.windowToken, 0)
        }

        bottomSheetDialog.show()
    }

}
