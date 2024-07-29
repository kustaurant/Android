package com.example.kustaurant.presentation.ui.tier

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.kustaurant.R
import com.example.kustaurant.data.model.NonTieredRestaurantGroup
import com.example.kustaurant.data.model.Restaurant
import com.example.kustaurant.data.model.TierListData
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
    private val viewModel: TierViewModel by activityViewModels()
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
    }

    private fun observeViewModel() {
        viewModel.mapData.observe(viewLifecycleOwner) { mapData ->
            // naverMap이 초기화된 후에 updateMap을 호출
            if (::naverMap.isInitialized) {
                updateMap(mapData)
            } else {
                binding.mapView.getMapAsync {
                    naverMap = it
                    updateMap(mapData)
                }
            }
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

    @SuppressLint("SetTextI18n")
    private fun showRestaurantInfo(restaurant: Restaurant) {
        binding.apply {
            bottomSheet.findViewById<TextView>(R.id.restaurantName).text = restaurant.restaurantName
            bottomSheet.findViewById<TextView>(R.id.restaurantDetails).text = restaurant.restaurantCuisine + " | " + restaurant.restaurantPosition

            Glide.with(requireContext())
                .load(restaurant.restaurantImgUrl)
                .placeholder(R.drawable.img_default_restaurant)
                .into(bottomSheet.findViewById(R.id.restaurantImage))

            val tierImageView = bottomSheet.findViewById<ImageView>(R.id.restaurantTier)
            val tierImageResource = when (restaurant.mainTier) {
                1 -> R.drawable.ic_rank_1
                2 -> R.drawable.ic_rank_2
                3 -> R.drawable.ic_rank_3
                4 -> R.drawable.ic_rank_4
                else -> R.drawable.ic_rank_all
            }
            tierImageView.setImageResource(tierImageResource)

            // 파트너십 정보 설정
            val partnershipInfo = if (restaurant.partnershipInfo.isEmpty()) {
                R.string.restaurant_no_partnership_info
            } else {
                restaurant.partnershipInfo
            }
            bottomSheet.findViewById<TextView>(R.id.restaurantPartnershipInfo).text =
                partnershipInfo.toString()
        }
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }



    private fun updateMarkersForZoom() {
        viewModel.mapData.value?.let { mapData ->
            updateMap(mapData)
        }
    }

    private fun updateMap(mapData: TierListData) {
        clearOverlaysAndMarkers()

        mapData.solidLines.forEach { line ->
            if (line.isNotEmpty()) {
                val polyline = PolylineOverlay().apply {
                    coords = line + line.first()
                    color = ContextCompat.getColor(requireContext(), R.color.polygon_line)
                    width = 2
                    joinType = PolylineOverlay.LineJoin.Round
                    map = naverMap
                }
                polylineOverlays.add(polyline)

                val polygon = PolygonOverlay().apply {
                    coords = line
                    color = PolygonColors.POLYGON_COLOR_WITH_ALPHA
                    outlineWidth = 0
                    map = naverMap
                }
                polygonOverlays.add(polygon)
            } else {
                Log.d("TierMapFragment", "Solid Line is empty")
            }
        }

        mapData.dashedLines.forEach { line ->
            if (line.isNotEmpty()) {
                val polyline = PolylineOverlay().apply {
                    coords = line + line.first()
                    color = ContextCompat.getColor(requireContext(), R.color.polygon_line)
                    width = 2
                    joinType = PolylineOverlay.LineJoin.Round
                    map = naverMap
                }
                polyline.setPattern(10, 5)
                polylineOverlays.add(polyline)

                val polygon = PolygonOverlay().apply {
                    coords = line
                    color = PolygonColors.POLYGON_COLOR_WITH_ALPHA
                    outlineWidth = 0
                    map = naverMap
                }
                polygonOverlays.add(polygon)
            } else {
                Log.d("TierMapFragment", "Dashed Line is empty")
            }
        }

        createMarkersForTieredRestaurants(mapData.tieredRestaurants)
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
            4 -> OverlayImage.fromResource(R.drawable.ic_rank_4)
            else -> OverlayImage.fromResource(R.drawable.ic_no_rank_position)
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

    object PolygonColors {
        const val POLYGON_COLOR_WITH_ALPHA = 0x7F43AB38
    }
}