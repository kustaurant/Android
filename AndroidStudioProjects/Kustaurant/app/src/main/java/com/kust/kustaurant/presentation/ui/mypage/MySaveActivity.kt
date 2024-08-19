package com.kust.kustaurant.presentation.ui.mypage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kust.kustaurant.databinding.ActivityMySaveBinding

class MySaveActivity : AppCompatActivity() {
    lateinit var binding : ActivityMySaveBinding
    private var saveData : ArrayList<SaveRestaurantData> = arrayListOf()
    private var saveRestaurantAdapter : SaveRestaurantAdapter ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMySaveBinding.inflate(layoutInflater)

        initBack()
        initDummy()
        initRecyclerView()

        setContentView(binding.root)
    }

    private fun initBack() {
        binding.saveBtnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun initDummy() {
        saveData.addAll(
            arrayListOf(
                SaveRestaurantData("제주곤이칼국수 건대점", "https://search.pstatic.net/common/?autoRotate=true&type=w560_sharpen&src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20221219_73%2F1671415873694AWTMq_JPEG%2FDSC04440.jpg",
                    2, "한식","중문~어대"),
                SaveRestaurantData("카토카츠","\"https://search.pstatic.net/common/?autoRotate=true&type=w560_sharpen&src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20230215_93%2F16764318243254l73x_JPEG%2FA3E8D691-A31C-4584-8D06-84EF660E9743.jpeg",
                    1,"일식","중문~어대"),
                SaveRestaurantData("제주곤이칼국수 건대점", "https://search.pstatic.net/common/?autoRotate=true&type=w560_sharpen&src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20221219_73%2F1671415873694AWTMq_JPEG%2FDSC04440.jpg",
                    2, "한식","중문~어대"),
                SaveRestaurantData("카토카츠","\"https://search.pstatic.net/common/?autoRotate=true&type=w560_sharpen&src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20230215_93%2F16764318243254l73x_JPEG%2FA3E8D691-A31C-4584-8D06-84EF660E9743.jpeg",
                    1,"일식","중문~어대"),
                SaveRestaurantData("제주곤이칼국수 건대점", "https://search.pstatic.net/common/?autoRotate=true&type=w560_sharpen&src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20221219_73%2F1671415873694AWTMq_JPEG%2FDSC04440.jpg",
                    2, "한식","중문~어대"),
                SaveRestaurantData("카토카츠","\"https://search.pstatic.net/common/?autoRotate=true&type=w560_sharpen&src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20230215_93%2F16764318243254l73x_JPEG%2FA3E8D691-A31C-4584-8D06-84EF660E9743.jpeg",
                    1,"일식","중문~어대"),
                SaveRestaurantData("제주곤이칼국수 건대점", "https://search.pstatic.net/common/?autoRotate=true&type=w560_sharpen&src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20221219_73%2F1671415873694AWTMq_JPEG%2FDSC04440.jpg",
                    2, "한식","중문~어대"),
                SaveRestaurantData("카토카츠","\"https://search.pstatic.net/common/?autoRotate=true&type=w560_sharpen&src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20230215_93%2F16764318243254l73x_JPEG%2FA3E8D691-A31C-4584-8D06-84EF660E9743.jpeg",
                    1,"일식","중문~어대"),

                )
        )
    }

    private fun initRecyclerView() {
        saveRestaurantAdapter = SaveRestaurantAdapter(this, saveData)
        binding.saveRvRestaurant.adapter = saveRestaurantAdapter
        binding.saveRvRestaurant.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }
}