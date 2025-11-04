package com.kust.kustaurant.presentation.ui.mypage

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.kust.kustaurant.R
import com.kust.kustaurant.databinding.ActivityMyOpinionBinding
import com.kust.kustaurant.presentation.common.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyOpinionActivity : BaseActivity() {
    lateinit var binding : ActivityMyOpinionBinding
    private val viewModel: MyPageViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyOpinionBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        observeViewModel()
        initBack()
        initOpinion()
        submitOpinion()

        setContentView(binding.root)
    }

    private fun observeViewModel() {
        viewModel.toastMessage.observe(this, Observer { message ->
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun submitOpinion() {
        binding.btnSubmit.setOnClickListener {
            viewModel.postFeedBackData(binding.etOpinion.text.toString())
            Toast.makeText(this, "피드백을 주셔서 감사합니다!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun initBack() {
        binding.myFlBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun initOpinion() {
        binding.etOpinion.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrEmpty()) {
                    binding.btnSubmit.setBackgroundResource(R.drawable.all_radius_50_sig1)
                    binding.tvSubmit.setTextColor(Color.WHITE)
                } else {
                    binding.btnSubmit.setBackgroundResource(R.drawable.all_radius_50_cement3)
                    binding.tvSubmit.setTextColor(getColor(R.color.cement_4))
                }
            }
        })
    }
}