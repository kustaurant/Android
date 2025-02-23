package com.kust.kustaurant.presentation.ui.mypage

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.kust.kustaurant.R
import com.kust.kustaurant.databinding.ActivityMySaveBinding
import com.kust.kustaurant.presentation.ui.detail.DetailActivity
import com.kust.kustaurant.presentation.ui.detail.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel

@AndroidEntryPoint
class MySaveActivity : AppCompatActivity() {
    lateinit var binding : ActivityMySaveBinding
    private lateinit var saveRestaurantAdapter : SaveRestaurantAdapter
    private val viewModel: MyPageViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMySaveBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.loadMyFavoriteData()

        initBack()
        observeViewModel()
        initRecyclerView()

        setContentView(binding.root)
    }

    private fun observeViewModel() {
        viewModel.myFavoriteData.observe(this){ menuData ->
            if (menuData.isEmpty()) {
                binding.saveRvRestaurant.visibility = View.GONE
                binding.myLlSaveNone.visibility = View.VISIBLE
                Glide.with(this)
                    .load(R.drawable.ic_my_page_none)
                    .into(binding.myIvSaveNone)
            } else {
                binding.saveRvRestaurant.visibility = View.VISIBLE
                binding.myLlSaveNone.visibility = View.GONE
                saveRestaurantAdapter.submitList(menuData)
            }
        }
    }

    private fun initBack() {
        binding.myFlBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun initRecyclerView() {
        saveRestaurantAdapter = SaveRestaurantAdapter(this)
        binding.saveRvRestaurant.adapter = saveRestaurantAdapter
        binding.saveRvRestaurant.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        saveRestaurantAdapter.setOnItemClickListener(object : SaveRestaurantAdapter.OnItemClickListener{
            override fun onSaveClicked(restaurantId: Int) {
                val intent = Intent(this@MySaveActivity, DetailActivity::class.java)
                intent.putExtra("restaurantId", restaurantId)
                startActivity(intent)
            }

        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadMyFavoriteData()
    }
}