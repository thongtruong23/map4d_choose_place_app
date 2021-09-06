package com.truongthong.map4d.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.truongthong.map4d.R
import com.truongthong.map4d.databinding.ActivityPlaceBinding
import com.truongthong.map4d.models.PlaceResult
import com.truongthong.map4d.viewmodel.PlaceViewModel

class PlaceActivity : AppCompatActivity(), PlaceUpdate {

    private lateinit var binding: ActivityPlaceBinding
    private lateinit var placeViewModel: PlaceViewModel

//    private val viewModel by lazy {
//        ViewModelProvider(this).get(PlaceViewModel::class.java)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        placeViewModel= ViewModelProvider(this).get(PlaceViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_place)

        binding.viewModel = placeViewModel
        binding.lifecycleOwner = this

        placeViewModel.backClick.observe(this, {
            if (it) {
                onBackPressed()
                placeViewModel.onBackCliked()
            }
        })

        showPlaceResult()
        handelStateEDT()

        placeViewModel.stateBtn.observe(this, {
            when {
                it -> {
                    binding.btnApply.setOnClickListener {
                        val intent = Intent()
                        val bundle = Bundle()

                        bundle.putParcelable("PROVINCE_VALUE", placeViewModel.provincePlaceResult.value)
                        bundle.putParcelable("DISTRICT_VALUE", placeViewModel.districtPlaceResult.value)
                        bundle.putParcelable(
                            "SUBDISTRICT_VALUE",
                            placeViewModel.subDistrictPlaceResult.value
                        )
                        intent.putExtra("BUNDLE", bundle)
                        setResult(33, intent)
                        finish()
                    }
                }
            }
            placeViewModel.handleStateButton()
        })
    }

    private fun showPlaceResult() {
        binding.txtProvince.setOnClickListener {
            val fragmentDialogChoosePlace = FragmentDialogChoosePlace()
            val bundle = Bundle()

            bundle.putParcelable("TYPE", TypePlace.PROVINCE)
            fragmentDialogChoosePlace.arguments = bundle
            fragmentDialogChoosePlace.show(supportFragmentManager, null)
        }

        placeViewModel.stateEdtDistrict.observe(this, {
            when {
                it -> {
                    binding.txtDistrict.setOnClickListener {
                        val fragmentDialogChoosePlace = FragmentDialogChoosePlace()
                        val bundle = Bundle()

                        bundle.putParcelable("TYPE", TypePlace.DISTRICT)
                        bundle.putString("CODE", placeViewModel.provinceCode.value)
                        fragmentDialogChoosePlace.arguments = bundle
                        fragmentDialogChoosePlace.show(supportFragmentManager, null)
                    }
                }
            }
        })

        placeViewModel.stateEdtSubDistrict.observe(this, {
            when {
                it -> {
                    binding.txtSubDistrict.setOnClickListener {
                        val fragmentDialogChoosePlace = FragmentDialogChoosePlace()
                        val bundle = Bundle()

                        bundle.putParcelable("TYPE", TypePlace.SUBDISTRICT)
                        bundle.putString("CODE", placeViewModel.districtCode.value)
                        fragmentDialogChoosePlace.arguments = bundle
                        fragmentDialogChoosePlace.show(supportFragmentManager, null)
                    }
                }
            }

        })
    }

    private fun handelStateEDT() {
        placeViewModel.provincePlaceResult.observe(this, {
            when (it) {
                null -> {
                    placeViewModel.onStateEDTProvinceVisible()
                    placeViewModel.onStateEDTDistrictDisable()
                    placeViewModel.onStateEDTSubDistrictDisable()
                }
                else -> {
                    placeViewModel.onStateEDTProvinceVisible()
                    placeViewModel.onStateEDTDistrictVisible()
                    placeViewModel.onStateEDTSubDistrictDisable()
                }
            }
        })

        placeViewModel.districtPlaceResult.observe(this, {
            when {
                it != null -> {
                    placeViewModel.onStateEDTSubDistrictVisible()
                }
                else -> {
                    placeViewModel.onStateEDTSubDistrictDisable()
                }
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        endProces(0, Intent())
    }

    private fun endProces(i: Int, intent: Intent) {
        setResult(i, intent)
        finish()
    }

    override fun dataListener(typePlace: TypePlace, placeResult: PlaceResult) {
        placeViewModel.handleCountryDetail(typePlace, placeResult)
    }
}