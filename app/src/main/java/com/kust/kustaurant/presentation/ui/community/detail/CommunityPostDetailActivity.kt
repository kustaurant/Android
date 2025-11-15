package com.kust.kustaurant.presentation.ui.community.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import coil.decode.SvgDecoder
import coil.load
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kust.kustaurant.R
import com.kust.kustaurant.databinding.ActivityCommunityPostDetailBinding
import com.kust.kustaurant.databinding.BottomSheetCommentBinding
import com.kust.kustaurant.databinding.PopupCommuPostDetailDotsBinding
import com.kust.kustaurant.domain.model.community.PostCommentStatus
import com.kust.kustaurant.presentation.common.BaseActivity
import com.kust.kustaurant.presentation.model.CommunityPostIntent
import com.kust.kustaurant.presentation.model.UiState
import com.kust.kustaurant.presentation.ui.community.write.CommunityPostWriteActivity
import com.kust.kustaurant.presentation.ui.splash.StartActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommunityPostDetailActivity : BaseActivity() {
    private var _binding: ActivityCommunityPostDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CommunityPostDetailViewModel by viewModels()
    private lateinit var commentAdapter: CommunityPostDetailCommentAdapter
    private var postId = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCommunityPostDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.svCommunityPostContent.visibility = View.INVISIBLE
        binding.progressIndicator.visibility = View.VISIBLE

        initPostContent()
        initRecyclerView()
        setupButton()
        setupObservers()
    }

    private fun initPostContent() {
        Log.d("postId", postId.toString())
        postId = intent.getLongExtra("postId", -1L)

        if (postId == -1L) {
            Toast.makeText(this, "잘못된 접근입니다.", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            viewModel.loadCommunityPostDetail(postId)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupObservers() {
        viewModel.postDetail.observe(this) { postDetail ->
            postDetail?.let { post ->
                binding.communityTvActivityPostInfo.text = post.category.displayName
                binding.communityTvPostTitle.text = post.title
                binding.communityTvPostUser.text = post.writernickname
                binding.communityTvTimeAgo.text = post.timeAgo
                binding.communityTvVisitCnt.text = post.visitCount.toString()
                binding.communityTvLikeCnt.text = post.likeOnlyCount.toString()
                binding.communityTvCommentsCnt.text = post.commentCount.toString()
                binding.communityTvTimeAgo.text = post.timeAgo
                binding.communityIvPostDetailDots.visibility = if (post.isPostMine) View.VISIBLE else View.GONE
                binding.communityTvBtnPostLike.text = post.likeOnlyCount.toString()
                binding.communityTvBtnScrapLike.text = post.scrapCount.toString()

                loadPostBodyToWebView(post.body)

                binding.communityIvUserIcon.load(post.writericonUrl) {
                    crossfade(true)
                    placeholder(R.drawable.ic_baby_cow)
                    error(R.drawable.ic_baby_cow)

                    if (post.writericonUrl?.endsWith(".svg", true) == true) {
                        decoderFactory(SvgDecoder.Factory())
                    }
                }
            }
        }

        viewModel.postComments.observe(this) { comments ->
            commentAdapter.submitList(comments.orEmpty())
        }

        viewModel.postCommentsCnt.observe(this) { cnt ->
            binding.communityTvCommentsCnt.text = cnt.toString()
        }

        viewModel.postScrapInfo.observe(this) { result ->
            binding.communityTvBtnScrapLike.text = result.postScrapCount.toString()
        }

        viewModel.postLikeInfo.observe(this) { result ->
            binding.communityTvBtnPostLike.text = result.likeCount.toString()
            binding.communityTvLikeCnt.text = result.likeCount.toString()
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
                    background: rgba(234, 234, 234, 0.2); /* #EAEAEA(cement_2) */
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
        with(binding) {
            communityLlBtnPostLike.setOnClickListener {
                viewModel.postPostLike(postId)
            }
            communityLlBtnScrap.setOnClickListener {
                viewModel.postPostDetailScrap(postId, )
            }
            communityIvPostDetailDots.setOnClickListener {
                if (viewModel.postMine.value!!) {
                    showPopupWindow()
                }
            }
            btnBack.setOnClickListener {
                finish()
            }
            communityClCommentConfirm.setOnClickListener {
                val inputText = communityEtInput.text.toString()
                if (inputText.isNotBlank()) {
                    viewModel.postCreateCommentReply(inputText, postId, null)
                    communityEtInput.text.clear()
                    Toast.makeText(this@CommunityPostDetailActivity, "댓글이 등록되었습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    communityEtInput.error = "텍스트를 입력해주세요"
                    Toast.makeText(this@CommunityPostDetailActivity, "텍스트를 입력해주세요", Toast.LENGTH_SHORT).show()
                }
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
            val postIntent = viewModel.postDetail.value?.let {
                CommunityPostIntent(
                    it.postId, it.title, it.category.value, it.body
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
        commentAdapter = CommunityPostDetailCommentAdapter(this)
        binding.communityRvComment.layoutManager = LinearLayoutManager(this)
        binding.communityRvComment.adapter = commentAdapter
        binding.communityRvComment.itemAnimator = null

        commentAdapter.setOnItemClickListener(object :
            CommunityPostDetailCommentAdapter.OnItemClickListener {
            override fun onReportClicked(commentId: Long) {
                if(viewModel.hasLoginInfo()) {
                    //viewModel.postCommentReport(restaurantId, commentId)
                    Toast.makeText(this@CommunityPostDetailActivity, "신고가 접수되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onDeleteClicked(commentId: Long, status: PostCommentStatus) {
                if(viewModel.hasLoginInfo()) {
                    if(status != PostCommentStatus.ACTIVE) {
                        Toast.makeText(this@CommunityPostDetailActivity, "이미 삭제된 댓글입니다.", Toast.LENGTH_SHORT).show()
                        return
                    }
                    viewModel.deleteComment(postId, commentId)
                    Toast.makeText(this@CommunityPostDetailActivity, "댓글 삭제가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCommentClicked(commentId: Long) {
                if(viewModel.hasLoginInfo()) {
                    showBottomSheetInput(commentId)
                }
            }

            override fun onLikeClicked(commentId: Long, position: Int) {
                if(viewModel.hasLoginInfo()) {
                    viewModel.postCommentReact(commentId, "LIKE")
                }
            }

            override fun onDisLikeClicked(commentId: Long, position: Int) {
                if(viewModel.hasLoginInfo()) {
                    viewModel.postCommentReact(commentId, "DISLIKE")
                }
            }
        })

        commentAdapter.interactionListener =
            object : CommunityPostDetailCommentReplyAdapter.OnItemClickListener {
                override fun onReportClicked(commentId: Long) {
                    if(viewModel.hasLoginInfo()) {
                        //viewModel.postCommentReport(restaurantId, commentId)
                        Toast.makeText(this@CommunityPostDetailActivity, "신고가 접수되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onDeleteClicked(commentId: Long, status: PostCommentStatus) {
                    if(viewModel.hasLoginInfo()) {
                        if(status != PostCommentStatus.ACTIVE) {
                            Toast.makeText(this@CommunityPostDetailActivity, "이미 삭제된 댓글입니다.", Toast.LENGTH_SHORT).show()
                            return
                        }

                        viewModel.deleteComment(postId, commentId)
                        Toast.makeText(this@CommunityPostDetailActivity, "댓글 삭제가 완료되었습니다.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onLikeClicked(commentId: Long, position: Int) {
                    if(viewModel.hasLoginInfo()) {
                        viewModel.postCommentReact(commentId, "LIKE")
                    }
                }

                override fun onDisLikeClicked(commentId: Long, position: Int) {
                    if(viewModel.hasLoginInfo()) {
                        viewModel.postCommentReact(commentId, "DISLIKE")
                    }
                }
            }
    }

    private fun showBottomSheetInput(commentId: Long) {
        binding.communityClComment.visibility = View.GONE

        val bottomSheetDialog = BottomSheetDialog(this).apply {
            setCancelable(true)
            setCanceledOnTouchOutside(true)
        }

        val bottomSheetBinding = BottomSheetCommentBinding.inflate(layoutInflater, null, false)
        bottomSheetDialog.setContentView(bottomSheetBinding.root)

        val etInput = bottomSheetBinding.detailEtInput
        val btnSubmit = bottomSheetBinding.detailClCommentConfirm

        bottomSheetDialog.setOnShowListener {
            etInput.requestFocus()
            etInput.postDelayed({
                val imm = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.showSoftInput(etInput, InputMethodManager.SHOW_IMPLICIT)
            }, 100)
        }

        btnSubmit.setOnClickListener {
            val inputText = etInput.text.toString()
            if (inputText.isNotBlank()) {
                viewModel.postCreateCommentReply(inputText, postId, commentId)
                bottomSheetDialog.dismiss()
                Toast.makeText(this@CommunityPostDetailActivity, "대댓글이 등록되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                etInput.error = "텍스트를 입력해주세요"
                Toast.makeText(this@CommunityPostDetailActivity, "텍스트를 입력해주세요", Toast.LENGTH_SHORT).show()
            }
        }

        bottomSheetDialog.setOnDismissListener {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(etInput.windowToken, 0)
            binding.communityClComment.visibility = View.VISIBLE
        }

        bottomSheetDialog.show()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val sendButton = findViewById<View>(R.id.community_cl_comment_confirm)
            val sendRect = Rect()
            sendButton.getGlobalVisibleRect(sendRect)
            if (sendRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                return super.dispatchTouchEvent(ev)
            }

            val currentFocusView = currentFocus
            if (currentFocusView is EditText) {
                val outRect = Rect()
                currentFocusView.getGlobalVisibleRect(outRect)
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    currentFocusView.clearFocus()
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(currentFocusView.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
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