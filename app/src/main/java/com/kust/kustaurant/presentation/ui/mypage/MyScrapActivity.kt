package com.kust.kustaurant.presentation.ui.mypage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.kust.kustaurant.R
import com.kust.kustaurant.databinding.ActivityMyScrapBinding
import com.kust.kustaurant.presentation.common.BaseActivity
import com.kust.kustaurant.presentation.ui.community.CommunityPostDetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyScrapActivity : BaseActivity() {
    lateinit var binding : ActivityMyScrapBinding
    private val viewModel: MyPageViewModel by viewModels()
    lateinit var postAdapter: ScrapAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyScrapBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.loadMyScrapData()

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
        viewModel.myScrapData.observe(this) { scrapData ->
            if (scrapData.isEmpty()) {
                binding.scrapRvRestaurant.visibility = View.GONE
                binding.myLlScrapNone.visibility = View.VISIBLE
                Glide.with(this)
                    .load(R.drawable.ic_my_page_none)
                    .into(binding.myIvScrapNone)
            } else {
                binding.scrapRvRestaurant.visibility = View.VISIBLE
                binding.myLlScrapNone.visibility = View.GONE
                postAdapter.submitList(scrapData)
            }
        }
    }

    private fun initRecyclerView() {
        postAdapter = ScrapAdapter(this)
        binding.scrapRvRestaurant.adapter = postAdapter
        binding.scrapRvRestaurant.layoutManager = LinearLayoutManager(this)

        postAdapter.setOnItemClickListener(object: ScrapAdapter.OnItemClickListener{
            override fun onPostClicked(postId: Int) {
                val intent = Intent(this@MyScrapActivity, CommunityPostDetailActivity::class.java)
                Log.d("postIdBefore", postId.toString())
                intent.putExtra("postId", postId)
                startActivity(intent)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadMyScrapData()
    }
}