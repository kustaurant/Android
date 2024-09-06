package com.kust.kustaurant.presentation.ui.community

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.kust.kustaurant.databinding.ActivityCommunityPostDetailBinding
import com.kust.kustaurant.domain.model.CommunityPostComment

class CommunityPostDetailActivity : AppCompatActivity() {
    private val viewModel: CommunityViewModel by viewModels()

    private lateinit var binding: ActivityCommunityPostDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityPostDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 데이터 바인딩 설정
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        // Intent로 전달받은 postId를 이용해 ViewModel에서 데이터를 가져옴
        val postId = intent.getIntExtra("postId", -1)
        if (postId != -1) {
            viewModel.loadCommunityPostDetail(postId)
        }

        setupObservers()
        setupBackButton()
    }

    private fun setupObservers() {
        // CommunityPost 데이터를 관찰하여 UI 업데이트
        viewModel.communityPostDetail.observe(this) { postDetail ->
            postDetail?.let { post ->
                binding.communityTvActivityPostInfo.text = post.postCategory
                binding.communityTvActivityPostInfo.text = post.postTitle
                //binding.likeCountTextView.text = post.likeCount.toString()

                // Glide 사용하여 이미지 로드
                Glide.with(this)
                    .load(post.user.rankImg)
                    .into(binding.communityIvUserIcon)

                // 댓글 목록도 업데이트 가능 (RecyclerView로 설정)
                setupCommentsRecyclerView(post.postCommentList)
            }
        }
    }

    private fun setupBackButton() {
        binding.btnBack.setOnClickListener {
            finish() // 뒤로 가기 버튼 클릭 시 액티비티 종료
        }
    }

    private fun setupCommentsRecyclerView(commentList: List<CommunityPostComment>) {
        //val commentAdapter = CommentAdapter(commentList)
        //binding.commentsRecyclerView.layoutManager = LinearLayoutManager(this)
        //binding.commentsRecyclerView.adapter = commentAdapter
    }
}
