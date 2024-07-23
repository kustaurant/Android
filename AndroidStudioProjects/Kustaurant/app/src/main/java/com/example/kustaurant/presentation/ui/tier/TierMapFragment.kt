package com.example.kustaurant.presentation.ui.tier

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.kustaurant.R
import com.example.kustaurant.data.model.NonTieredRestaurantGroup
import com.example.kustaurant.data.model.Restaurant
import com.example.kustaurant.data.model.TierMapData
import com.example.kustaurant.databinding.FragmentTierMapBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.overlay.PolygonOverlay
import com.naver.maps.map.overlay.PolylineOverlay
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TierMapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentTierMapBinding
    private val viewModel: TierMapViewModel by viewModels()
    private lateinit var naverMap: NaverMap

    // 오버레이 리스트
    private val polygonOverlays = mutableListOf<PolygonOverlay>()
    private val polylineOverlays = mutableListOf<PolylineOverlay>()
    private val restaurantMarkers = mutableListOf<Marker>()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    private var currentZoom = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTierMapBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)

        // BottomSheet 설정
        val bottomSheet = binding.bottomSheet
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()

        // 필요한 쿼리 파라미터를 전달하여 데이터 로드
        val cuisines = "ALL"
        val situations = "ALL"
        val locations = "ALL"
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

        naverMap.setOnMapClickListener { _, coord ->
            handleMapClick(coord)
        }

        naverMap.addOnCameraChangeListener { _, _ ->
            val newZoom = naverMap.cameraPosition.zoom.toInt()
            if (newZoom != currentZoom) {
                currentZoom = newZoom
                updateMarkersForZoom()
            }
        }

    }

    private fun handleMapClick(coord: LatLng) {
        val restaurant = findRestaurantAtCoord(coord)

        if (restaurant != null) {
            showRestaurantInfo(restaurant)
        } else {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    private fun findRestaurantAtCoord(coord: LatLng): Restaurant? {
        val allRestaurants = (viewModel.mapData.value?.tieredRestaurants ?: emptyList()) +
                (viewModel.mapData.value?.nonTieredRestaurants?.flatMap { it.restaurants } ?: emptyList())

        return allRestaurants.find { restaurant ->
            val restaurantCoord = LatLng(restaurant.y, restaurant.x)
            coord.latitude == restaurantCoord.latitude && coord.longitude == restaurantCoord.longitude
        }
    }

    private fun showRestaurantInfo(restaurant: Restaurant) {
        binding.apply {
            bottomSheet.findViewById<TextView>(R.id.restaurantName).text = restaurant.restaurantName
            bottomSheet.findViewById<TextView>(R.id.restaurantCuisine).text = restaurant.restaurantCuisine

            Glide.with(requireContext())
                .load(restaurant.restaurantImgUrl)
                .placeholder(R.drawable.img_default_restaurant)
                .into(bottomSheet.findViewById(R.id.restaurantImage))

            val tierImageView = bottomSheet.findViewById<ImageView>(R.id.tierImage)
            val tierImageResource = when (restaurant.mainTier) {
                1 -> R.drawable.ic_rank_1
                2 -> R.drawable.ic_rank_2
                3 -> R.drawable.ic_rank_3
                else -> R.drawable.ic_star
            }
            tierImageView.setImageResource(tierImageResource)
        }
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun updateMarkersForZoom() {
        viewModel.mapData.value?.let { mapData ->
            updateMap(mapData)
        }
    }
    private fun updateMap(mapData: TierMapData) {
        // 기존 오버레이 및 마커 제거
        clearOverlaysAndMarkers()

        // 폴리곤 오버레이 생성
//        if (mapData.polygonCoords.isNotEmpty()) {
//            val polygon = PolygonOverlay().apply {
//                coords = mapData.polygonCoords
//                color = 0x7F43AB38 // 반투명한 초록색
//                outlineColor = 0xFF00FF00.toInt() // 초록색 테두리
//                outlineWidth = 2 // 테두리 두께
//                map = naverMap
//            }
//            polygonOverlays.add(polygon)
//        } else {
//            Log.d("TierMapFragment", "Polygon Coords are empty")
//        }

        // 실선 오버레이 및 내부 영역 생성
        mapData.solidLines.forEach { line ->
            if (line.isNotEmpty()) {
                // PolylineOverlay 생성
                val polyline = PolylineOverlay().apply {
                    coords = line
                    color = 0xFF00FF00.toInt()
                    width = 2
                    joinType = PolylineOverlay.LineJoin.Round
                    map = naverMap
                }
                polylineOverlays.add(polyline)

                // PolygonOverlay 생성
                val polygon = PolygonOverlay().apply {
                    coords = line
                    color = 0x7F43AB38 // 반투명한 초록색
                    outlineColor = 0xFF00FF00.toInt() // 초록색 테두리
                    outlineWidth = 0 // 테두리 두께
                    map = naverMap
                }
                polygonOverlays.add(polygon)
            } else {
                Log.d("TierMapFragment", "Solid Line is empty")
            }
        }

        // 점선 오버레이 및 내부 영역 생성
        mapData.dashedLines.forEach { line ->
            if (line.isNotEmpty()) {
                // PolylineOverlay 생성
                val polyline = PolylineOverlay().apply {
                    coords = line
                    color = 0xFF00FF00.toInt()
                    width = 2
                    joinType = PolylineOverlay.LineJoin.Round
                    map = naverMap
                }
                polyline.setPattern(10, 5)
                polylineOverlays.add(polyline)

                // PolygonOverlay 생성
                val polygon = PolygonOverlay().apply {
                    coords = line
                    color = 0x7F43AB38 // 반투명한 초록색
                    outlineColor = 0xFF00FF00.toInt() // 초록색 테두리
                    outlineWidth = 0 // 테두리 두께
                    map = naverMap
                }
                polygonOverlays.add(polygon)
            } else {
                Log.d("TierMapFragment", "Dashed Line is empty")
            }
        }

        // 티어가 있는 레스토랑 마커 생성
        createMarkersForTieredRestaurants(mapData.tieredRestaurants)

        // 티어가 없는 레스토랑 마커 생성 (줌 레벨에 따라 필터링)
        createMarkersForNonTieredRestaurants(mapData.nonTieredRestaurants)
    }

    private fun clearOverlaysAndMarkers() {
        polygonOverlays.forEach { it.map = null }
        polygonOverlays.clear()
        polylineOverlays.forEach { it.map = null }
        polylineOverlays.clear()
        restaurantMarkers.forEach { it.map = null }
        restaurantMarkers.clear()
    }

    private fun createMarkersForTieredRestaurants(tieredRestaurants: List<Restaurant>) {
        tieredRestaurants.forEach { restaurant ->
            createRestaurantMarker(restaurant)
        }
    }

    private fun createMarkersForNonTieredRestaurants(nonTieredRestaurants: List<NonTieredRestaurantGroup>) {
        nonTieredRestaurants.filter { it.zoom <= currentZoom }.forEach { group ->
            group.restaurants.forEach { restaurant ->
                createRestaurantMarker(restaurant)
            }
        }
    }

    private fun createRestaurantMarker(restaurant: Restaurant) {
        val marker = Marker().apply {
            position = LatLng(restaurant.y, restaurant.x)
            captionText = restaurant.restaurantName
            icon = getMarkerIcon(restaurant.mainTier)
            map = naverMap

            setOnClickListener {
                showRestaurantInfo(restaurant)
                true
            }
        }
        restaurantMarkers.add(marker)
    }

    private fun getMarkerIcon(tier: Int): OverlayImage {
        return when (tier) {
            1 -> OverlayImage.fromResource(R.drawable.ic_rank_1)
            2 -> OverlayImage.fromResource(R.drawable.ic_rank_2)
            3 -> OverlayImage.fromResource(R.drawable.ic_rank_3)
            else -> OverlayImage.fromResource(R.drawable.ic_star)
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