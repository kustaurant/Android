package com.kust.kustaurant.presentation.ui.search

import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kust.kustaurant.databinding.ActivitySearchBinding
import com.kust.kustaurant.domain.model.SearchRestaurant

class SearchActivity : AppCompatActivity() {
    lateinit var binding: ActivitySearchBinding
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var restaurantList: List<SearchRestaurant>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchBtnBack.setOnClickListener {
            finish()
        }

        // 어댑터를 빈 리스트로 초기화
        searchAdapter = SearchAdapter(emptyList())
        binding.searchRv.adapter = searchAdapter

        // RecyclerView 레이아웃 매니저 설정
        binding.searchRv.layoutManager = LinearLayoutManager(this)

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

    // 검색을 수행하고 RecyclerView를 업데이트하는 함수
    private fun performSearch() {
        val query = binding.searchEt.text.toString()
        if (query.isNotEmpty()) {
            // 여기서 실제 서버 호출을 수행할 수 있음
            // 현재는 더미 데이터로 시뮬레이션
            val dummyData = listOf(
                SearchRestaurant(1, 1, "부탄츄", "Korean", "Downtown", "url_to_image", 3, true, false, 37.56, 126.97, null, 4.5),
                SearchRestaurant(2, 2, "샤브로", "Japanese", "Uptown", "url_to_image", 2, false, true, 37.56, 126.98, null, 4.2)
            )

            // 더미 데이터로 어댑터 업데이트
            searchAdapter.updateData(dummyData)
        } else {
            // 검색어가 비어있을 경우 RecyclerView를 비움
            searchAdapter.updateData(emptyList())
        }
    }
}