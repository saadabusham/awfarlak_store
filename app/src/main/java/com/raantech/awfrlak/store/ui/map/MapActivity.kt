package com.raantech.awfrlak.store.ui.map

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import com.google.android.gms.location.LocationSettingsStates
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.jakewharton.rxbinding3.widget.textChangeEvents
import com.raantech.awfrlak.R
import com.raantech.awfrlak.store.data.common.Constants
import com.raantech.awfrlak.store.data.models.map.Address
import com.raantech.awfrlak.databinding.ActivityMapBinding
import com.raantech.awfrlak.store.ui.base.activity.BaseBindingActivity
import com.raantech.awfrlak.store.ui.map.adapter.PlacesAdapter
import com.raantech.awfrlak.store.utils.displayLocationSettingsRequest
import com.raantech.awfrlak.store.utils.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import io.nlopez.smartlocation.SmartLocation
import io.reactivex.disposables.Disposable
import permissions.dispatcher.NeedsPermission
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MapActivity : BaseBindingActivity<ActivityMapBinding>(), OnMapReadyCallback {

    private var googleMap: GoogleMap? = null

    private val placesClient by lazy { Places.createClient(this) }

    private val token by lazy { AutocompleteSessionToken.newInstance() }

    private var disposable: Disposable? = null

    private lateinit var mapFragment: SupportMapFragment

    lateinit var adapter: PlacesAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            R.layout.activity_map,
            hasToolbar = true,
            hasBackButton = true,
            showBackArrow = true,
            hasTitle = false
        )
        setUp()
    }
    private fun setUp(){
        mapFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentMap) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if (!Places.isInitialized()) {
            Places.initialize(this, getString(R.string.google_maps_key))
        }
        initAutoCompletePlaces()
        init()
        setUpListeners()
    }


    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun init() {
        enableLocationServices()
    }

    private fun enableLocationServices() {
        if (!SmartLocation.with(this).location().state()
                .locationServicesEnabled()
        ) {
            displayLocationSettingsRequest(
                REQUEST_CODE_LOCATION
            )
        } else {
            getCurrentLocation()
        }
    }

    private fun getCurrentLocation() {
        requestLocationPermission(object : MyMultiPermissionListeners {
            override fun onPermissionGranted() {
                binding?.progressBar?.relativeProgress?.visible()
                SmartLocation.with(this@MapActivity)
                    .location()
                    .oneFix()
                    .start { location ->
                        zoomToLocation(location.latitude, location.longitude)
                    }
            }

            override fun onPermissionDenied() {
            }

        }, true)

    }

    private fun zoomToLocation(latitude: Double, longitude: Double) {
        googleMap?.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(latitude, longitude),
                ZOOM_LEVEL
            )
        )
        binding?.progressBar?.relativeProgress?.gone()
    }

    private fun setUpListeners() {

        binding?.btnNext?.setOnClickListener {
            onNextClicked()
        }
        binding?.fabGetCurrentLocation?.setOnClickListener {
            onGetMyLocationClicked()
        }
    }

    fun onNextClicked() {
        val location = getLatLng()
        if (location == null) {
            showErrorAlert(
                resources.getString(R.string.location),
                resources.getString(R.string.please_pick_location)
            )
            return
        }
        val address = location?.let { Address(it.latitude, it.longitude) }
        setResult(RESULT_OK,Intent().apply {
            this.putExtra(Constants.BundleData.ADDRESS,address)
        })
        finish()
    }

    private fun getLatLng(): LatLng? {
        val array = IntArray(2)
        mapFragment.view?.getLocationOnScreen(array)
        val mapX = array[0]
        val mapY = array[1]

        val mapWidth = mapFragment.view?.width ?: 0
        val mapHeight = mapFragment.view?.height ?: 0
        val mapCenterX = (mapX + mapWidth / 2)
        val mapCenterY = (mapY + mapHeight / 2)
        return googleMap?.projection?.fromScreenLocation(Point(mapCenterX, mapCenterY))
    }

    fun onGetMyLocationClicked() {
        enableLocationServices()
    }

    private fun initAutoCompletePlaces() {
        disposable =
            binding?.autocompleteTxtLocation?.textChangeEvents()
                ?.debounce(2L, TimeUnit.MICROSECONDS)
                ?.subscribe {
                    if (it.text.isNotEmpty()) {
                        val request =
                            FindAutocompletePredictionsRequest
                                .builder()
                                .setQuery(it.text.toString())
                                .setTypeFilter(TypeFilter.ESTABLISHMENT)
                                .setSessionToken(token)
                                .build()

                        placesClient.findAutocompletePredictions(request).addOnSuccessListener {
                            adapter = PlacesAdapter(this, it.autocompletePredictions)
                            binding?.autocompleteTxtLocation?.setAdapter(adapter)
                            binding?.autocompleteTxtLocation?.showDropDown()

                        }

                    }

                }

        binding?.autocompleteTxtLocation?.setOnItemClickListener { parent, view, position, id ->
            binding?.autocompleteTxtLocation?.setText(
                adapter.getItem(position)?.getFullText(null).toString()
            )
            adapter.getItem(position)?.placeId?.let { getPlace(it) }
        }
    }

    private fun getPlace(placeId: String) {
        val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
        val request = FetchPlaceRequest.builder(placeId, placeFields).build()
        placesClient.fetchPlace(request)
            .addOnSuccessListener { p0 ->
                binding?.root.hideKeyboard(this)
                googleMap?.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        p0?.place?.latLng,
                        14f
                    )
                )
            }
    }

    override fun onMapReady(p0: GoogleMap?) {
        googleMap = p0
        googleMap?.uiSettings?.isCompassEnabled = false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_LOCATION) {
            val states = LocationSettingsStates.fromIntent(data)
            when (resultCode) {
                RESULT_OK -> {
                    enableLocationServices()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        onRequestPermissionsResult(requestCode, grantResults)
    }


    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }


    companion object {

        const val REQUEST_CODE_LOCATION = 99
        const val ZOOM_LEVEL = 16f
        fun start(
            context: Context?
        ) {
            val intent = Intent(context, MapActivity::class.java)
            context?.startActivity(intent)
        }

        fun start(
            context: Activity?,
            resultLauncher: ActivityResultLauncher<Intent>
        ) {
            val intent = Intent(context, MapActivity::class.java)
            resultLauncher.launch(intent)
        }
    }

}