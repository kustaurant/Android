package com.kust.kustaurant.presentation.ui.mypage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kust.kustaurant.databinding.ActivityMyCommentBinding
import com.kust.kustaurant.presentation.ui.community.CommunityPostDetailActivity
import com.kust.kustaurant.presentation.ui.mypage.MyPostActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyCommentActivity : AppCompatActivity() {
    lateinit var binding : ActivityMyCommentBinding
    private var commentData : ArrayList<CommentData> = arrayListOf()
    private lateinit var commentAdapter : CommentAdapter
    private val viewModel: MyPageViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyCommentBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

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
        viewModel.loadMyCommentData()
        viewModel.myCommentData.observe(this){ commentData ->
            commentAdapter.submitList(commentData)
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
}