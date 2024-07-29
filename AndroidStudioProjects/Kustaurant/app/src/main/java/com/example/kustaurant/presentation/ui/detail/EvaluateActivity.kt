package com.example.kustaurant.presentation.ui.detail

import android.os.Bundle
import android.widget.AdapterView.OnItemClickListener
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kustaurant.R
import com.example.kustaurant.databinding.ActivityEvaluateBinding

class EvaluateActivity : AppCompatActivity() {
    lateinit var binding : ActivityEvaluateBinding
    lateinit var keyWordAdapter : EvaluateKeyWordAdapter
    var keyWordList : ArrayList<String> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEvaluateBinding.inflate(layoutInflater)

        initKeyWord()
        initRecyclerView()

        setContentView(binding.root)
    }

    private fun initKeyWord() {
        keyWordList.addAll(
            arrayListOf(
                "혼밥", "2~4인", "5인 이상", "단체 회식", "배달", "야식", "친구초대", "데이트", "소개팅")
        )
    }

    private fun initRecyclerView() {
        keyWordAdapter = EvaluateKeyWordAdapter(keyWordList)
        binding.rvKeyword.adapter = keyWordAdapter
        binding.rvKeyword.layoutManager = GridLayoutManager(this, 4)

        keyWordAdapter.setItemClickListener(object : EvaluateKeyWordAdapter.OnItemClickListener{
            override fun onItemClicked() {

            }

        })
    }
}