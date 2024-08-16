package com.kust.kustaurant.presentation.ui.detail

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RatingBar
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.kust.kustaurant.R
import com.kust.kustaurant.databinding.ActivityEvaluateBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EvaluateActivity : AppCompatActivity() {
    lateinit var binding: ActivityEvaluateBinding
    private lateinit var keyWordAdapter: EvaluateKeyWordAdapter
    private var keyWordList: ArrayList<String> = arrayListOf()
    private lateinit var ratingBar: RatingBar
    private val viewModel: DetailViewModel by viewModels()
    private lateinit var photoPickerLauncher: ActivityResultLauncher<Intent>
    private val REQ_GALLERY = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEvaluateBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val restaurantId = intent.getIntExtra("restaurantId", 1)
        Log.d("restaurantId", restaurantId.toString())
        viewModel.loadEvaluateData(restaurantId)

        initKeyWord()
        initRecyclerView()
        initPhotoPicker()
        initPlusPhoto()
        initRatingBar()
        setContentView(binding.root)
    }

    private fun initPhotoPicker() {
        photoPickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.data?.let { uri: Uri ->
                    binding.cvPlushPhoto.visibility = View.VISIBLE
                    binding.ivPlusPhoto.setImageURI(uri)
                }
            }
        }
    }

    private fun initPlusPhoto() {
        binding.btnPhotoPlus.setOnClickListener {
            selectGallery()
        }
    }

    private fun selectGallery() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), REQ_GALLERY)
        } else {
            openGallery()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        photoPickerLauncher.launch(intent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQ_GALLERY && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGallery()
        }
    }

    private fun initRatingBar() {
        ratingBar = binding.rbEvaluate
        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            binding.tvEvaluateHow.text = when (rating) {
                0f, 0.5f -> "아직 선택하지 않으셨습니다."
                1f, 1.5f -> "맘에 안듬"
                2f, 2.5f, 3f, 3.5f -> "괜찮아요 다음에 또 방문할 것 같아요"
                4f, 4.5f -> "매우 좋아요"
                5f -> "굿"
                else -> "선택오류"
            }
            if (rating == 0f) {
                binding.btnSubmit.setBackgroundResource(R.drawable.all_radius_50_cement3)
                binding.btnSubmit.setTextColor(getColor(R.color.cement_4))
            } else {
                binding.btnSubmit.setBackgroundResource(R.drawable.all_radius_50_sig1)
                binding.btnSubmit.setTextColor(Color.WHITE)
            }
        }
    }

    private fun initKeyWord() {
        keyWordList.addAll(arrayListOf("혼밥", "2~4인", "5인 이상", "단체 회식", "배달", "야식", "친구초대", "데이트", "소개팅"))
    }

    private fun initRecyclerView() {
        keyWordAdapter = EvaluateKeyWordAdapter(keyWordList)
        binding.rvKeyword.adapter = keyWordAdapter
        binding.rvKeyword.layoutManager = GridLayoutManager(this, 4)
    }
}
