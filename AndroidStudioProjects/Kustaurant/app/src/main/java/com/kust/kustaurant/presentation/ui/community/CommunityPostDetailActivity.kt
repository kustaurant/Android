package com.kust.kustaurant.presentation.ui.community

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.kust.kustaurant.databinding.ActivityCommunityPostDetailBinding
import com.kust.kustaurant.domain.model.CommunityPostComment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommunityPostDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCommunityPostDetailBinding
    private val viewModel: CommunityPostDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityPostDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 데이터 바인딩 설정
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // Intent로 전달받은 postId를 이용해 ViewModel에서 데이터를 가져옴
        val postId = intent.getIntExtra("postId", -1)
        if (postId != -1) {
            viewModel.loadCommunityPostDetail(postId)
        }
        else {
            Toast.makeText(this, "올바르지 않은 게시글 ID 입니다, 새로고침 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
        }

        setupObservers()
        setupBackButton()
    }
    private fun setupObservers() {
        viewModel.communityPostDetail.observe(this) { postDetail ->
            postDetail?.let { post ->
                binding.communityTvActivityPostInfo.text = post.postCategory
                binding.communityTvActivityPostInfo.text = post.postTitle
                binding.communityTvPostUser.text = post.user.userNickname
                binding.communityTvTimeAgo.text = post.timeAgo
                binding.communityTvVisitCnt.text = post.postVisitCount
                binding.communityTvLikeCnt.text = post.likeCount.toString()
                binding.communityTvCommentsCnt.text = post.commentCount.toString()
                binding.communityTvTimeAgo.text = post.timeAgo

                binding.communityTvBtnPostLike.text = post.likeCount.toString()
                binding.communityTvBtnScrapLike.text = post.scrapCount.toString()

                loadPostBodyToWebView(post.postBody)

                // 사용자 아이콘 이미지 로드
                Glide.with(this)
                    .load(post.user.rankImg)
                    .into(binding.communityIvUserIcon)

                // 댓글 목록 업데이트
                setupCommentsRecyclerView(post.postCommentList)
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


    private fun setupBackButton() {
        binding.btnBack.setOnClickListener {
            finish() // 뒤로 가기 버튼 클릭 시 액티비티 종료
        }
    }

    private fun setupCommentsRecyclerView(commentList: List<CommunityPostComment>) {
//        val commentAdapter = CommentAdapter(commentList)
//        binding.commentsRecyclerView.layoutManager = LinearLayoutManager(this)
//        binding.commentsRecyclerView.adapter = commentAdapter
    }
}
