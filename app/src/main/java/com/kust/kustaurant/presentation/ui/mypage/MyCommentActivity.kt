package com.kust.kustaurant.presentation.ui.mypage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.kust.kustaurant.R
import com.kust.kustaurant.databinding.ActivityMyCommentBinding
import com.kust.kustaurant.presentation.common.BaseActivity
import com.kust.kustaurant.presentation.ui.community.detail.CommunityPostDetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyCommentActivity : BaseActivity() {
    lateinit var binding : ActivityMyCommentBinding
    private lateinit var commentAdapter : CommentAdapter
    private val viewModel: MyPageViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyCommentBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.loadMyCommentData()

        initBack()
        observeViewModel()
        initRecyclerView()

        setContentView(binding.root)
    }

    private fun initBack() {
        binding.myFlBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun observeViewModel() {
        viewModel.myCommentData.observe(this){ commentData ->
            if (commentData.isEmpty()) {
                binding.commentRvRestaurant.visibility = View.GONE
                binding.myLlPostNone.visibility = View.VISIBLE
                Glide.with(this)
                    .load(R.drawable.ic_my_page_none)
                    .into(binding.myIvPostNone)
            } else {
                binding.commentRvRestaurant.visibility = View.VISIBLE
                binding.myLlPostNone.visibility = View.GONE
                commentAdapter.submitList(commentData)
            }
        }
    }

    private fun initRecyclerView() {
        commentAdapter = CommentAdapter(this)
        binding.commentRvRestaurant.adapter = commentAdapter
        binding.commentRvRestaurant.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        commentAdapter.setOnItemClickListener(object: CommentAdapter.OnItemClickListener{
            override fun onCommentClicked(postId: Int) {
                val intent = Intent(this@MyCommentActivity, CommunityPostDetailActivity::class.java)
                Log.d("postIdBefore", postId.toString())
                intent.putExtra("postId", postId)
                startActivity(intent)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadMyCommentData()
    }
}