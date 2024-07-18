package com.example.kustaurant.presentation.ui.tier

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kustaurant.databinding.FragmentTierMapBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.PolygonOverlay

class TierMapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentTierMapBinding
    private lateinit var mapView: MapView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTierMapBinding.inflate(inflater, container, false)

        // MapView 초기화 및 설정
        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        return binding.root
    }

    override fun onMapReady(naverMap: NaverMap) {
        val cameraUpdate = CameraUpdate.scrollTo(LatLng(37.5407, 127.0738))
        naverMap.moveCamera(cameraUpdate)

        // 폴리곤 좌표 설정
        val konkukPolygonCoords = listOf(
            LatLng(37.5407, 127.0738),
            LatLng(37.5419, 127.0735),
            LatLng(37.5425, 127.0740),
            LatLng(37.5430, 127.0745),
            LatLng(37.5435, 127.0750),
            LatLng(37.5440, 127.0755),
            LatLng(37.5445, 127.0760),
            LatLng(37.5450, 127.0765)
        )

        // 폴리곤 오버레이 생성
        val polygon = PolygonOverlay().apply {
            coords = konkukPolygonCoords
            color = 0x7F00FF00 // 반투명한 초록색
            outlineColor = 0xFF00FF00.toInt() // 초록색 테두리
            outlineWidth = 5 // 테두리 두께
        }

        // 폴리곤을 지도에 추가
        polygon.map = naverMap
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}