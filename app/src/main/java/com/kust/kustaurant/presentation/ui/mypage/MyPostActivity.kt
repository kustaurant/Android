package com.kust.kustaurant.presentation.ui.mypage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.kust.kustaurant.R
import com.kust.kustaurant.databinding.ActivityMyPostBinding
import com.kust.kustaurant.presentation.ui.community.CommunityPostDetailActivity
import com.kust.kustaurant.presentation.ui.detail.DetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPostActivity : AppCompatActivity() {
    lateinit var binding : ActivityMyPostBinding
    private val viewModel: MyPageViewModel by viewModels()
    lateinit var postAdapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPostBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.loadMyCommunityData()

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
        viewModel.myCommunityData.observe(this) { postData ->
            if (postData.isEmpty()) {
                binding.myRvPost.visibility = View.GONE
                binding.myLlPostNone.visibility = View.VISIBLE
                Glide.with(this)
                    .load(R.drawable.ic_my_page_none)
                    .into(binding.myIvPostNone)
            } else {
                binding.myRvPost.visibility = View.VISIBLE
                binding.myLlPostNone.visibility = View.GONE
                postAdapter.submitList(postData)
            }
        }
    }

    private fun initRecyclerView() {
        postAdapter = PostAdapter(this)
        binding.myRvPost.adapter = postAdapter
        binding.myRvPost.layoutManager = LinearLayoutManager(this)

        postAdapter.setOnItemClickListener(object: PostAdapter.OnItemClickListener{
            override fun onPostClicked(postId: Int) {
                val intent = Intent(this@MyPostActivity, CommunityPostDetailActivity::class.java)
                Log.d("postIdBefore", postId.toString())
                intent.putExtra("postId", postId)
                startActivity(intent)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadMyCommunityData()
    }
}