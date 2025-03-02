package com.kust.kustaurant.presentation.ui.tier

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kust.kustaurant.R
import com.kust.kustaurant.data.getAccessToken
import com.kust.kustaurant.data.model.NonTieredRestaurantGroup
import com.kust.kustaurant.data.model.TierMapData
import com.kust.kustaurant.databinding.FragmentTierMapBinding
import com.kust.kustaurant.domain.model.TierRestaurant
import com.kust.kustaurant.presentation.ui.detail.DetailActivity
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
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

    // 오버레이 및 마커 리스트
    private val polygonOverlays = mutableListOf<PolygonOverlay>()
    private val polylineOverlays = mutableListOf<PolylineOverlay>()
    private val restaurantMarkers = mutableListOf<Marker>()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>

    private var currentZoom = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTierMapBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val mapView = binding.tierMapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        if (getAccessToken(requireContext()) == null) {
            viewModel.loadRestaurant(TierScreenType.MAP, false)
        } else {
            viewModel.loadRestaurant(TierScreenType.MAP, true)
        }

        val bottomSheet = binding.tierBottomSheet
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
            isDraggable = false
            isHideable = true
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.mapData.observe(viewLifecycleOwner) { mapData ->
            if (::naverMap.isInitialized) {
                updateMap(mapData)
                moveCameraToVisibleBounds(mapData.visibleBounds)
            } else {
                binding.tierMapView.getMapAsync {
                    naverMap = it
                    updateMap(mapData)
                }
            }
        }

        viewModel.isShowBottomSheet.observe(viewLifecycleOwner) { isShow ->
            if (isShow) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }
    }

    override fun onMapReady(map: NaverMap) {
        naverMap = map
        naverMap.uiSettings.isZoomControlEnabled = false

        naverMap.setOnMapClickListener { _, _ ->
            viewModel.setShowBottomSheet(false)
        }

        naverMap.addOnCameraChangeListener { _, _ ->
            val newZoom = naverMap.cameraPosition.zoom.toInt()
            if (newZoom != currentZoom) {
                currentZoom = newZoom
                updateMarkersForZoom()
            }

            if (viewModel.isShowBottomSheet.value == true) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }

    private fun updateMarkersForZoom() {
        viewModel.mapData.value?.let { mapData ->
            updateMap(mapData)
        }
    }

    private fun updateMap(mapData: TierMapData) {
        clearOverlaysAndMarkers()

        mapData.solidLines.forEach { line ->
            if (line.isNotEmpty()) {
                val polyline = PolylineOverlay().apply {
                    coords = line + line.first()
                    color = ContextCompat.getColor(requireContext(), R.color.polygon_line)
                    width = 7
                    joinType = PolylineOverlay.LineJoin.Round
                    map = naverMap
                }
                polylineOverlays.add(polyline)

                val polygon = PolygonOverlay().apply {
                    coords = line
                    color = PolygonColors.POLYGON_SELECTED
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
                    width = 7
                    joinType = PolylineOverlay.LineJoin.Round
                    map = naverMap
                }
                polyline.setPattern(10, 5)
                polylineOverlays.add(polyline)

                val polygon = PolygonOverlay().apply {
                    coords = line
                    color = PolygonColors.POLYGON_UNSELECTED
                    outlineWidth = 0
                    map = naverMap
                }
                polygonOverlays.add(polygon)
            } else {
                Log.d("TierMapFragment", "Dashed Line is empty")
            }
        }

        createMarkersForTieredRestaurants(mapData.tieredTierRestaurants)
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

    private fun createMarkersForTieredRestaurants(tieredTierRestaurants: List<TierRestaurant>) {
        tieredTierRestaurants.forEach { restaurant ->
            createRestaurantMarker(restaurant)
        }
    }

    private fun createMarkersForNonTieredRestaurants(nonTieredRestaurants: List<NonTieredRestaurantGroup>) {
        nonTieredRestaurants.filter { it.zoom <= currentZoom }.forEach { group ->
            group.tierRestaurants.forEach { restaurant ->
                createRestaurantMarker(restaurant)
            }
        }
    }

    private fun createRestaurantMarker(tierRestaurant: TierRestaurant) {
        val marker = Marker().apply {
            position = LatLng(tierRestaurant.y, tierRestaurant.x)
            icon = getMarkerIcon(tierRestaurant)
            map = naverMap
            zIndex = if (tierRestaurant.isFavorite) {
                5
            } else {
                when (tierRestaurant.mainTier) {
                    1 -> 4
                    2 -> 3
                    3 -> 2
                    4 -> 1
                    else -> 0
                }
            }
            setOnClickListener {
                showRestaurantInfo(tierRestaurant)
                true
            }
        }
        restaurantMarkers.add(marker)
    }

    private fun getMarkerIcon(restaurant: TierRestaurant): OverlayImage {
        return if (restaurant.isFavorite) {
            OverlayImage.fromResource(R.drawable.ic_favorite)
        } else {
            when (restaurant.mainTier) {
                1 -> OverlayImage.fromResource(R.drawable.ic_rank_1)
                2 -> OverlayImage.fromResource(R.drawable.ic_rank_2)
                3 -> OverlayImage.fromResource(R.drawable.ic_rank_3)
                4 -> OverlayImage.fromResource(R.drawable.ic_rank_4)
                else -> OverlayImage.fromResource(R.drawable.ic_no_rank_position)
            }
        }
    }

    private fun showRestaurantInfo(tierRestaurant: TierRestaurant) {
        binding.bottomSheetContent.apply {
            restaurant = tierRestaurant
            Glide.with(requireContext())
                .load(tierRestaurant.restaurantImgUrl)
                .placeholder(R.drawable.img_default_restaurant)
                .into(tierIvRestaurantImg)
            val tierImageResource = when (tierRestaurant.mainTier) {
                1 -> R.drawable.ic_rank_1
                2 -> R.drawable.ic_rank_2
                3 -> R.drawable.ic_rank_3
                4 -> R.drawable.ic_rank_4
                else -> R.drawable.ic_rank_all
            }
            tierIvRestaurantTierImg.setImageResource(tierImageResource)
        }
        binding.tierBottomSheet.setOnClickListener {
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("restaurantId", tierRestaurant.restaurantId)
            startActivity(intent)
        }
        viewModel.setShowBottomSheet(true)
    }

    private fun moveCameraToVisibleBounds(visibleBounds: List<Double>) {
        if (visibleBounds.size < 4) return
        val minLng = visibleBounds[0]
        val maxLng = visibleBounds[1]
        val minLat = visibleBounds[2]
        val maxLat = visibleBounds[3]
        val bounds = LatLngBounds(
            LatLng(minLat, minLng),
            LatLng(maxLat, maxLng)
        )
        val cameraUpdate = CameraUpdate.fitBounds(bounds, 50)
        naverMap.moveCamera(cameraUpdate)
    }

    override fun onStart() {
        super.onStart()
        binding.tierMapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.tierMapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.tierMapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.tierMapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.tierMapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.tierMapView.onLowMemory()
    }

    object PolygonColors {
        val POLYGON_SELECTED = Color.argb(59, 67, 171, 56)
        val POLYGON_UNSELECTED = Color.argb(31, 67, 171, 56)
    }
}
