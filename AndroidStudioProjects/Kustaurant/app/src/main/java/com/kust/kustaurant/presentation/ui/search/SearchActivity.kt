package com.kust.kustaurant.presentation.ui.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kust.kustaurant.databinding.ActivitySearchBinding
import com.kust.kustaurant.domain.model.SearchRestaurant
import com.kust.kustaurant.presentation.ui.detail.DetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var searchAdapter: SearchAdapter

    private val searchViewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupObservers()
        setupListeners()
    }

    private fun setupRecyclerView() {
        searchAdapter = SearchAdapter(emptyList())
        binding.searchRv.adapter = searchAdapter
        binding.searchRv.layoutManager = LinearLayoutManager(this)

        searchAdapter.setOnItemClickListener(object : SearchAdapter.OnItemClickListener {
            override fun onItemClicked(data: SearchRestaurant) {
                val intent = Intent(this@SearchActivity, DetailActivity::class.java)
                intent.putExtra("restaurantId", data.restaurantId)
                startActivity(intent)
            }
        })
    }

    private fun setupObservers() {
        searchViewModel.searchResults.observe(this) { results ->
            searchAdapter.updateData(results)
            if (results.isEmpty()) {
                Toast.makeText(this, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show() // 검색 결과가 없는 경우 사용자에게 알림
            }
        }

        searchViewModel.error.observe(this) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, "오류 발생: $it", Toast.LENGTH_SHORT).show() // 오류 메시지 표시
            }
        }
    }

    private fun setupListeners() {
        binding.searchBtnBack.setOnClickListener {
            finish()
        }

        binding.searchEt.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableRight = 2 // 오른쪽 drawable 인덱스
                if (event.rawX >= (binding.searchEt.right - binding.searchEt.compoundDrawables[drawableRight].bounds.width())) {
                    performSearch()
                    return@setOnTouchListener true
                }
            }
            false
        }
    }

    private fun performSearch() {
        val query = binding.searchEt.text.toString()
        Log.d("search","${query}")
        if (query.isNotEmpty()) {
            searchViewModel.searchRestaurants(query)
        } else {
            searchAdapter.updateData(emptyList()) // 검색어가 비어있을 경우 RecyclerView 비우기
            Toast.makeText(this, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show() // 빈 검색어 알림 추가
        }
    }
}