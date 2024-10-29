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
import com.bumptech.glide.Glide
import com.google.android.flexbox.FlexboxLayoutManager
import com.kust.kustaurant.R
import com.kust.kustaurant.data.model.EvaluationDataResponse
import com.kust.kustaurant.databinding.ActivityEvaluateBinding
import com.kust.kustaurant.presentation.ui.search.SearchActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EvaluateActivity : AppCompatActivity() {
    lateinit var binding: ActivityEvaluateBinding
    private lateinit var keyWordAdapter: EvaluateKeyWordAdapter
    private var keyWordList: ArrayList<String> = arrayListOf()
    private lateinit var ratingBar: RatingBar
    private var isEvaluated = false
    private val viewModel: DetailViewModel by viewModels()
    private lateinit var photoPickerLauncher: ActivityResultLauncher<Intent>
    private var restaurantId = 1
    private val REQ_GALLERY = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEvaluateBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        restaurantId = intent.getIntExtra("restaurantId", 1)
        isEvaluated = intent.getBooleanExtra("isEvaluated", false)
        viewModel.loadEvaluateData(restaurantId)

        if(isEvaluated){
           viewModel.loadMyEvaluationData(restaurantId)
            binding.btnSubmit.text = "다시 평가하기"
        }

        observeViewModel()

        initBack()
        initKeyWord()
        initRecyclerView()
        initPhotoPicker()
        initPlusPhoto()
        initRatingBar()
        initSearch()
        submitEvaluate()
        setContentView(binding.root)
    }

    private fun initSearch() {
        binding.evaluateFlIvSearch.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observeViewModel() {
        viewModel.evaluationData.observe(this) { data ->
            updateEvaluation(data)
        }
    }

    private fun updateEvaluation(data: EvaluationDataResponse){
        ratingBar.rating = data.evaluationScore.toFloat()
        binding.etEvaluate.setText(data.evaluationComment)
        val commentForRating = data.starComments.find { it.star.toFloat() == ratingBar.rating }?.comment
        binding.tvEvaluateHow.text = commentForRating ?: "선택한 별점에 대한 코멘트가 없습니다."

        if(data.evaluationImgUrl != null){
            binding.cvPlushPhoto.visibility = View.VISIBLE
            Glide.with(this)
                .load(data.evaluationImgUrl)
                .into(binding.ivPlusPhoto)
        }
        keyWordAdapter.setSelectedKeywords(data.evaluationSituations)
    }

    private fun initBack() {
        binding.evaluateFlIvBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()

        }
    }

    private fun initPhotoPicker() {
        photoPickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.data?.let { uri: Uri ->
                    binding.cvPlushPhoto.visibility = View.VISIBLE
                    binding.ivPlusPhoto.setImageURI(uri)
                    binding.ivPlusPhoto.tag = uri  // 태그에 Uri 저장
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
                0f -> "아직 선택하지 않으셨습니다."
                0.5f -> "다시 갈 것 같진 않아요."
                1f -> "많이 아쉬워요"
                1.5f -> "다른데 갈껄"
                2f -> "조금 아쉬워요."
                2.5f -> "무난했어요."
                3f -> "괜찮았어요."
                3.5f -> "만족스러웠어요."
                4f -> "무조건 한번 더 올 것 같아요."
                4.5f -> "행복했습니다."
                5f -> "인생 최고의 식당입니다."
                else -> "선택오류"
            }
            if (rating == 0f) {
                binding.btnSubmit.setBackgroundResource(R.drawable.all_radius_50_cement3)
                binding.btnSubmit.setTextColor(getColor(R.color.cement_4))
                binding.btnSubmit.isEnabled = false
            } else {
                binding.btnSubmit.setBackgroundResource(R.drawable.all_radius_50_sig1)
                binding.btnSubmit.setTextColor(Color.WHITE)
                binding.btnSubmit.isEnabled = true
            }
        }
    }

    private fun initKeyWord() {
        keyWordList.addAll(arrayListOf("혼밥", "2~4인", "5인 이상", "단체 회식", "배달", "야식", "친구초대", "데이트", "소개팅"))
    }

    private fun initRecyclerView() {
        keyWordAdapter = EvaluateKeyWordAdapter(keyWordList)
        binding.rvKeyword.adapter = keyWordAdapter
        binding.rvKeyword.layoutManager = FlexboxLayoutManager(this)
    }

    private fun submitEvaluate() {
        binding.btnSubmit.setOnClickListener {
            val rating = ratingBar.rating
            val comment = binding.etEvaluate.text.toString()
            val keywords = keyWordAdapter.getSelectedItems() // 선택된 키워드 목록
            Log.d("taejung", keywords.toString())
            val imageUrl = (binding.ivPlusPhoto.tag as? Uri)  // 이미지 URI 저장을 위한 방법 중 하나
            viewModel.postEvaluationData(this , restaurantId, rating.toDouble(), comment, keywords, imageUrl)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        if(isEvaluated){
            viewModel.loadMyEvaluationData(restaurantId)
            binding.btnSubmit.text = "다시 평가하기"
        }
    }
}
