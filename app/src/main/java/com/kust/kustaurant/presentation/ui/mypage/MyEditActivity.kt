package com.kust.kustaurant.presentation.ui.mypage

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.kust.kustaurant.R
import com.kust.kustaurant.databinding.ActivityMyEditBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MyEditActivity : AppCompatActivity() {
    lateinit var binding : ActivityMyEditBinding
    private val viewModel: MyPageViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.loadMyProfileData()

        observeViewModel()
        initBack()
        setEditChanged()
        patchInfo()
    }

    private fun observeViewModel() {
        viewModel.updateSuccess.observe(this, Observer { success ->
            if (success) {
                finish()
            }
        })

        viewModel.toastMessage.observe(this, Observer { message ->
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setEditChanged() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkForChanges()
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        binding.editEtName.addTextChangedListener(textWatcher)
        binding.editEtPhone.addTextChangedListener(textWatcher)
        binding.editEtEmail.isEnabled = false
    }

    private fun checkForChanges() {
        val hasChanged = viewModel.hasProfileChanged(
            binding.editEtName.text.toString(),
            binding.editEtEmail.text.toString(),
            binding.editEtPhone.text.toString()
        )
        updateForChanges(hasChanged)
    }

    private fun updateForChanges(hasChanged: Boolean) {
        if (hasChanged) {
            binding.btnSubmit.setBackgroundResource(R.drawable.all_radius_50_sig1)
            binding.tvSubmit.setTextColor(Color.WHITE)
            binding.btnSubmit.isEnabled = true
        } else {
            binding.btnSubmit.setBackgroundResource(R.drawable.all_radius_50_cement3)
            binding.tvSubmit.setTextColor(getColor(R.color.cement_4))
            binding.btnSubmit.isEnabled = false

        }
    }

    private fun patchInfo() {
        binding.btnSubmit.setOnClickListener {
            viewModel.patchMyProfileData(
                binding.editEtName.text.toString(),
                binding.editEtEmail.text.toString(),
                binding.editEtPhone.text.toString()
            )
        }
    }

    private fun initBack() {
        binding.myFlBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}