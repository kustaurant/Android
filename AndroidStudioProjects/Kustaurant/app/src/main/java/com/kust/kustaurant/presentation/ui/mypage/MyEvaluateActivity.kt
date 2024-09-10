package com.kust.kustaurant.presentation.ui.mypage

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.kust.kustaurant.R
import com.kust.kustaurant.databinding.ActivityMyEvaluateBinding
import com.kust.kustaurant.presentation.ui.detail.DetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyEvaluateActivity : AppCompatActivity() {
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
            evaluateRestaurantAdapter.submitList(data)
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
}