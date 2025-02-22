package com.kust.kustaurant.presentation.ui.community

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kust.kustaurant.R
import com.kust.kustaurant.data.getAccessToken
import com.kust.kustaurant.databinding.ActivityCommunityPostDetailBinding
import com.kust.kustaurant.databinding.PopupCommuPostDetailDotsBinding
import com.kust.kustaurant.presentation.model.CommunityPostIntent
import com.kust.kustaurant.presentation.model.UiState
import com.kust.kustaurant.presentation.ui.splash.StartActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

@AndroidEntryPoint
class CommunityPostDetailActivity : AppCompatActivity() {
    private var _binding: ActivityCommunityPostDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CommunityPostDetailViewModel by viewModels()
    private lateinit var CommuCommentAdapter: CommunityPostDetailCommentAdapter
    private var postId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCommunityPostDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewModel = viewModel

        binding.svCommunityPostContent.visibility = View.INVISIBLE
        binding.progressIndicator.visibility = View.VISIBLE

        initPostContent()
        initRecyclerView()
        setupButton()
        setupObservers()
    }

    private fun initPostContent() {
        Log.d("postId", postId.toString())
        postId = intent.getIntExtra("postId", -1)

        if (postId == -1) {
            Toast.makeText(this, "잘못된 접근입니다.", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            viewModel.loadCommunityPostDetail(postId)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupObservers() {
        viewModel.communityPostDetail.observe(this) { postDetail ->
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

        viewModel.postScrapInfo.observe(this) { result ->
            binding.communityTvBtnScrapLike.text = result.scrapCount.toString()
        }

        viewModel.postLikeInfo.observe(this) { result ->
            binding.communityTvBtnPostLike.text = result.likeCount.toString()
        }

        viewModel.postDelete.observe(this) { result ->
            if (result) {
                Toast.makeText(this, "게시글이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                viewModel.resetPostDelete()
                finish()
            }
        }

        viewModel.uiState.observe(this) { uiState ->
            when (uiState) {
                is UiState.Error.Forbidden -> {
                    // Err code : 403
                    Toast.makeText(this, "로그인이 필요한 서비스입니다.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, StartActivity::class.java)
                    startActivity(intent)
                    finish()
                    viewModel.resetUiState()
                }
                is UiState.Error.Other -> {
                    Toast.makeText(this, uiState.message, Toast.LENGTH_SHORT).show()
                    viewModel.resetUiState()
                }
                is UiState.Loading -> {
                    viewModel.resetUiState()
                }
                is UiState.Idle, is UiState.Success<*> -> {
                }
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadPostBodyToWebView(postBody: String) {
        val webView = binding.communityWvMiddleContent
        webView.settings.javaScriptEnabled = false

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.svCommunityPostContent.visibility = View.VISIBLE
                binding.progressIndicator.visibility = View.GONE
            }
        }

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
        viewModel.postMine.value?.let{
            binding.communityIvPostDetailDots.isVisible = viewModel.postMine.value!!
        }

        binding.communityLlBtnPostLike.background = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = 100f
            setStroke(1, ContextCompat.getColor(this@CommunityPostDetailActivity, R.color.signature_1))
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.communityLlBtnPostLike.setOnClickListener {
            viewModel.postPostLike(postId)
        }
        binding.communityLlBtnScrap.setOnClickListener {
            viewModel.postPostDetailScrap(postId)
        }

        binding.communityIvPostDetailDots.setOnClickListener {
            if (viewModel.postMine.value!!) {
                showPopupWindow()
            }
        }
    }

    private fun showPopupWindow() {
        val popupBinding = PopupCommuPostDetailDotsBinding.inflate(layoutInflater)
        val popupWindow = PopupWindow(
            popupBinding.root,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )

        popupWindow.showAsDropDown(binding.communityIvPostDetailDots)

        popupBinding.clDelete.setOnClickListener {
            viewModel.deletePost(postId)
            popupWindow.dismiss()
        }


        popupBinding.clModify.setOnClickListener {
            val postIntent = viewModel.communityPostDetail.value?.let {
                CommunityPostIntent(
                    it.postId, it.postTitle, it.postCategory, it.postBody
                )
            }
            finish()

            val intent = Intent(this, CommunityPostWriteActivity::class.java).apply {
                putExtra("postSummary", postIntent)
            }

            popupWindow.dismiss()
            startActivity(intent)
        }
    }


    private fun initRecyclerView() {
        CommuCommentAdapter = CommunityPostDetailCommentAdapter(this)
        binding.communityRvComment.layoutManager = LinearLayoutManager(this)
        binding.communityRvComment.adapter = CommuCommentAdapter
        binding.communityRvComment.itemAnimator = null

        CommuCommentAdapter.setOnItemClickListener(object :
            CommunityPostDetailCommentAdapter.OnItemClickListener {
            override fun onReportClicked(commentId: Int) {
                checkToken {
                    //viewModel.postCommentReport(restaurantId, commentId)
                    Toast.makeText(this@CommunityPostDetailActivity, "신고가 접수되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onDeleteClicked(commentId: Int) {
                checkToken {
                    viewModel.deleteComment(postId, commentId)
                    Toast.makeText(this@CommunityPostDetailActivity, "댓글 삭제가 완료되었습니다.", Toast.LENGTH_SHORT).show()
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
                        Toast.makeText(this@CommunityPostDetailActivity, "신고가 접수되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onDeleteClicked(commentId: Int) {
                    checkToken {
                        viewModel.deleteComment(postId, commentId)
                        Toast.makeText(this@CommunityPostDetailActivity, "댓글 삭제가 완료되었습니다.", Toast.LENGTH_SHORT)
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
        val accessToken = getAccessToken(this)
        if (accessToken == null) {
            val intent = Intent(this, StartActivity::class.java)
            startActivity(intent)
        } else {
            action()
        }
    }

    private fun showBottomSheetInput(commentId: Int) {
        val bottomSheetDialog = BottomSheetDialog(this).apply {
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
                    getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.showSoftInput(etInput, InputMethodManager.SHOW_IMPLICIT)
            }, 100)
        }

        btnSubmit.setOnClickListener {
            val inputText = etInput.text.toString()
            if (inputText.isNotBlank()) {
                viewModel.postCreateCommentReply(
                    inputText,
                   postId.toString(),
                    commentId.toString()
                )
                bottomSheetDialog.dismiss()
                Toast.makeText(this@CommunityPostDetailActivity, "대댓글이 등록되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                etInput.error = "텍스트를 입력해주세요"
                Toast.makeText(this@CommunityPostDetailActivity, "텍스트를 입력해주세요", Toast.LENGTH_SHORT).show()
            }
        }

        bottomSheetDialog.setOnDismissListener {
            val imm =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(etInput.windowToken, 0)
        }

        bottomSheetDialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()

        binding.communityWvMiddleContent.apply {
            clearHistory()
            clearCache(true)
            loadUrl("about:blank")
            onPause()
            removeAllViews()
            destroy()
        }

        // View 및 어댑터 참조 해제
        binding.communityRvComment.adapter = null
        _binding = null
    }
}