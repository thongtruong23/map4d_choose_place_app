package com.truongthong.map4d.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.truongthong.map4d.R
import com.truongthong.map4d.databinding.ActivityMainBinding
import com.truongthong.map4d.models.PlaceResult
import com.truongthong.map4d.viewmodel.MainViewModel
import com.truongthong.map4d.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import vn.map4d.map.core.Map4D
import vn.map4d.map.core.OnMapReadyCallback

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private var map4D : Map4D? = null
    private var locationPermissionGranted = false

    private val mainViewModel by lazy {
        ViewModelProvider(this, MainViewModelFactory(this.application)).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableMyLocation()

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.viewModel = mainViewModel

        binding.mapView.getMapAsync(this)

        mainViewModel.buttonClick.observe(this, {
            if (it){
                startActivityForResult(Intent(this, PlaceActivity::class.java), 33)
                mainViewModel.onClickToSearchSuccess()
            }
        })

        binding.lifecycleOwner = this

    }

    override fun onMapReady(map4D: Map4D) {
        this.map4D = map4D

        updateUI()
    }

    private fun enableMyLocation(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationPermissionGranted = false
        when(requestCode){
            LOCATION_PERMISSION_REQUEST_CODE ->{
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    locationPermissionGranted = true
                }else{
                    locationPermissionGranted = false
                    enableMyLocation()
                }
            }
        }
        updateUI()
    }

    private fun updateUI() {
        try {
            if (locationPermissionGranted){
                map4D?.isMyLocationEnabled = true
                map4D?.uiSettings?.isMyLocationButtonEnabled = true
            }else{
                map4D?.isMyLocationEnabled = false
                map4D?.uiSettings?.isMyLocationButtonEnabled = false
            }
        }catch (e: SecurityException){
            print(e.message)
        }
    }

    companion object{
        const val LOCATION_PERMISSION_REQUEST_CODE = 100
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(resultCode){

            33 -> {
                val bundle = data?.getBundleExtra("BUNDLE")
                val province: PlaceResult? = bundle?.getParcelable("PROVINCE_VALUE")

                var district = PlaceResult()
                if (bundle?.getParcelable<PlaceResult>("DISTRICT_VALUE") != null){
                    district = bundle.getParcelable("DISTRICT_VALUE")!!
                }

                var subDistrict = PlaceResult()
                if (bundle?.getParcelable<PlaceResult>("SUBDISTRICT_VALUE") != null){
                    subDistrict = bundle.getParcelable("SUBDISTRICT_VALUE")!!
                }
                if (province != null) {
                    mainViewModel.handleChoosePlace(province, district, subDistrict)
                }
            }


        }
    }


}