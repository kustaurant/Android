package com.kust.kustaurant.presentation.ui.mypage

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kust.kustaurant.databinding.ActivityMySaveBinding
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

        initBack()
        observeViewModel()
        initRecyclerView()

        setContentView(binding.root)
    }

    private fun observeViewModel() {
        viewModel.loadMyFavoriteData()
        viewModel.myFavoriteData.observe(this){ menuData ->
            saveRestaurantAdapter.submitList(menuData)
        }
    }

    private fun initBack() {
        binding.saveBtnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun initRecyclerView() {
        saveRestaurantAdapter = SaveRestaurantAdapter(this)
        binding.saveRvRestaurant.adapter = saveRestaurantAdapter
        binding.saveRvRestaurant.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }
}