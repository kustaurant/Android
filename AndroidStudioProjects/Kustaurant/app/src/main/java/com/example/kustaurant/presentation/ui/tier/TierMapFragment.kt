package com.example.kustaurant.presentation.ui.tier

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.kustaurant.databinding.FragmentTierMapBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.PolygonOverlay
import com.naver.maps.map.overlay.PolylineOverlay
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TierMapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentTierMapBinding
    private val viewModel: TierMapViewModel by viewModels()
    private lateinit var naverMap: NaverMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTierMapBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()

        // 필요한 쿼리 파라미터를 전달하여 데이터 로드
        val cuisines = "KO,WE,AS 또는 ALL 또는 JH"
        val situations = "ONE,SEVEN 또는 ALL"
        val locations = "L1,L2,L3 또는 ALL"
        viewModel.loadMapData(cuisines, situations, locations)
    }

    private fun observeViewModel() {
        viewModel.mapData.observe(viewLifecycleOwner) { mapData ->
            updateMap(mapData)
        }
    }

    override fun onMapReady(map: NaverMap) {
        naverMap = map
        val cameraUpdate = CameraUpdate.scrollTo(LatLng(37.5407, 127.0738))
        naverMap.moveCamera(cameraUpdate)

        // ViewModel을 통해 데이터 로드
        val cuisines = "KO,WE,AS 또는 ALL 또는 JH"
        val situations = "ONE,SEVEN 또는 ALL"
        val locations = "L1,L2,L3 또는 ALL"
        viewModel.loadMapData(cuisines, situations, locations)
    }

    private fun updateMap(mapData: TierMapData) {
        // 폴리곤 오버레이 생성
        val polygon = PolygonOverlay().apply {
            coords = mapData.polygonCoords
            color = 0x7F00FF00 // 반투명한 초록색
            outlineColor = 0xFF00FF00.toInt() // 초록색 테두리
            outlineWidth = 5 // 테두리 두께
        }
        polygon.map = naverMap

        // 실선 오버레이 생성
        mapData.solidLines.forEach { line ->
            val polyline = PolylineOverlay().apply {
                coords = line
                color = Color.BLUE
                width = 5
            }
            polyline.map = naverMap
        }

        // 점선 오버레이 생성
        mapData.dashedLines.forEach { line ->
            val polyline = PolylineOverlay().apply {
                coords = line
                color = Color.RED
                width = 5
            }
            polyline.map = naverMap
        }
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }
}
