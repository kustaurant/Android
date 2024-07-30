package com.example.kustaurant.presentation.ui.detail

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.widget.AdapterView.OnItemClickListener
import android.widget.RatingBar
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kustaurant.R
import com.example.kustaurant.databinding.ActivityEvaluateBinding

class EvaluateActivity : AppCompatActivity() {
    lateinit var binding : ActivityEvaluateBinding
    private lateinit var keyWordAdapter : EvaluateKeyWordAdapter
    private var keyWordList : ArrayList<String> = arrayListOf()
    private lateinit var ratingBar : RatingBar
    private lateinit var photoPickerLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEvaluateBinding.inflate(layoutInflater)

        initKeyWord()
        initRecyclerView()
        initPhotoPicker()
        initPlusPhoto()
        initRatingBar()
        setContentView(binding.root)
    }

    private fun initPhotoPicker() {
        photoPickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                binding.ivPlusPhoto.setImageURI(uri)
            }
        }
    }

    private fun initPlusPhoto() {
        binding.btnPhotoPlus.setOnClickListener {
            photoPickerLauncher.launch("image/*")
        }
    }

    private fun initRatingBar() {
        ratingBar = binding.rbEvaluate
        ratingBar.setOnRatingBarChangeListener{ _, rating, _ ->
            binding.tvEvaluateHow.text =
                when(rating){
                    0f, 0.5f -> "아직 선택하지 않으셨습니다."
                    1f, 1.5f -> "맘에 안듬"
                    2f, 2.5f -> "괜찮아요 다음에 또 방문할 것 같아요"
                    3f, 3.5f -> "괜찮아요 다음에 또 방문할 것 같아요"
                    4f, 4.5f -> "괜찮아요 다음에 또 방문할 것 같아요"
                    5f -> "굿"
                    else -> "선택오류"
                }
            if(rating == 0f){
                binding.btnSubmit.setBackgroundResource(R.drawable.all_radius_50_cement3)
                binding.btnSubmit.setTextColor(getColor(R.color.cement_4))
            } else {
                binding.btnSubmit.setBackgroundResource(R.drawable.all_radius_50_sig1)
                binding.btnSubmit.setTextColor(Color.WHITE)
            }
        }
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
    }

    private fun sendSelectedKeywords() {
        val selectedKeywords = keyWordAdapter.getSelectedItems()
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra("selectedKeywords", ArrayList(selectedKeywords))
        }
        startActivity(intent)
    }
}