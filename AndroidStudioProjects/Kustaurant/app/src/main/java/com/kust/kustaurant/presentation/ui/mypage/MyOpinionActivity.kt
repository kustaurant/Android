package com.kust.kustaurant.presentation.ui.mypage

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kust.kustaurant.R
import com.kust.kustaurant.databinding.ActivityMyOpinionBinding

class MyOpinionActivity : AppCompatActivity() {
    lateinit var binding : ActivityMyOpinionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyOpinionBinding.inflate(layoutInflater)

        initBack()
        initOpinion()

        setContentView(binding.root)
    }

    private fun initBack() {
        binding.opinionBtnBack.setOnClickListener {
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