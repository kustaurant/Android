package com.kust.kustaurant.presentation.ui.mypage

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.kust.kustaurant.R
import com.kust.kustaurant.databinding.ActivityMyEvaluateBinding
import com.kust.kustaurant.presentation.common.BaseActivity
import com.kust.kustaurant.presentation.ui.detail.DetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyEvaluateActivity : BaseActivity() {
    lateinit var binding : ActivityMyEvaluateBinding
    private lateinit var evaluateRestaurantAdapter : EvaluateRestaurantAdapter
    private val viewModel: MyPageViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyEvaluateBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.loadMyEvaluateData()

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
        viewModel.myEvaluateData.observe(this){ data ->
            if (data.isEmpty()) {
                binding.myRvEvaluate.visibility = View.GONE
                binding.myLlEvaluateNone.visibility = View.VISIBLE
                Glide.with(this)
                    .load(R.drawable.ic_my_page_none)
                    .into(binding.myIvEvaluateNone)
            } else {
                binding.myRvEvaluate.visibility = View.VISIBLE
                binding.myLlEvaluateNone.visibility = View.GONE
                evaluateRestaurantAdapter.submitList(data)
            }
        }
    }

    private fun initRecyclerView() {
        evaluateRestaurantAdapter = EvaluateRestaurantAdapter(this)
        binding.myRvEvaluate.adapter = evaluateRestaurantAdapter
        binding.myRvEvaluate.layoutManager = LinearLayoutManager(this)

        evaluateRestaurantAdapter.setOnItemClickListener(object: EvaluateRestaurantAdapter.OnItemClickListener{
            override fun onEvaluateClicked(restaurantId: Int) {
                val intent = Intent(this@MyEvaluateActivity, DetailActivity::class.java)
                intent.putExtra("restaurantId", restaurantId)
                startActivity(intent)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadMyEvaluateData()
    }
}