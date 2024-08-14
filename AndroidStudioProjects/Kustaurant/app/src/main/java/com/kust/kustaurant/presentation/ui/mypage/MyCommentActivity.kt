package com.kust.kustaurant.presentation.ui.mypage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kust.kustaurant.databinding.ActivityMyCommentBinding

class MyCommentActivity : AppCompatActivity() {
    lateinit var binding : ActivityMyCommentBinding
    private var commentData : ArrayList<CommentData> = arrayListOf()
    private var commentAdapter : CommentAdapter ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyCommentBinding.inflate(layoutInflater)

        initBack()
        initDummy()
        initRecyclerView()

        setContentView(binding.root)
    }

    private fun initBack() {
        binding.commentBtnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initDummy() {
        commentData.addAll(
            arrayListOf(
                CommentData("제주곤이칼국수 건대점", "심각한 버그 발견..",
                    "깜짝이야 ;", 0),
                CommentData("제주곤이칼국수 건대점", "심각한 버그 발견..",
                    "깜짝이야 ㅋㅋㅋ", 0),
                CommentData("제주곤이칼국수 건대점", "진짜 이 사이트 개 거품이다....;;;",
                    "극찬 ㄷㄷ", 0),
                CommentData("제주곤이칼국수 건대점", "다담주 알촌에서 정모합시다",
                    "....?", 0),
                CommentData("제주곤이칼국수 건대점", "칼바람 할 (28세, 남, 미혼, 무직) 구함",
                    "??", 0),
                CommentData("제주곤이칼국수 건대점", "칼바람 할 (28세, 남, 미혼, 무직) 구함",
                    "?", 0),
                CommentData("제주곤이칼국수 건대점", "댓글 글자제한 좀 늘려주십쇼 ㅠㅠㅠ",
                    "조금만 기다리시면 바로 늘려드리겠습니", 0))
        )
    }

    private fun initRecyclerView() {
        commentAdapter = CommentAdapter(this, commentData)
        binding.commentRvRestaurant.adapter = commentAdapter
        binding.commentRvRestaurant.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }
}