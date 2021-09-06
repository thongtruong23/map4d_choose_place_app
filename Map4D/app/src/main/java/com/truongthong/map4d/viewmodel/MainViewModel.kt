package com.truongthong.map4d.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.truongthong.map4d.R
import com.truongthong.map4d.models.PlaceResult
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val compositeDisposable = CompositeDisposable()

    private val provinceRX = PublishSubject.create<PlaceResult>()
    private val _province = MutableLiveData<PlaceResult>()
    val province: LiveData<PlaceResult>
        get() = _province

    private val districtRX = PublishSubject.create<PlaceResult>()
    private val _district = MutableLiveData<PlaceResult>()
    val district: LiveData<PlaceResult>
        get() = _district

    private val subDistrictRx = PublishSubject.create<PlaceResult>()
    private val _subDistrict = MutableLiveData<PlaceResult>()
    val subDistrict: LiveData<PlaceResult>
        get() = _subDistrict

    private val btnClick = PublishSubject.create<Boolean>()
    private val _btnClick = MutableLiveData<Boolean>()
    val buttonClick: LiveData<Boolean>
        get() = _btnClick

    private val _txtChoosePlace = MutableLiveData<String>()
    val txtChoosePlace: LiveData<String>
        get() = _txtChoosePlace

    init {
        btnPlaceClick()
        addProvinceState()
        addDistrictState()
        addSubDistrictState()
        _txtChoosePlace.value = app.getString(R.string.choose_place_search)
    }

    private fun addProvinceState() {
        provinceRX.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                println(it.name)
                _province.postValue(it)
            }.addTo(compositeDisposable)
    }

    private fun addDistrictState() {
        districtRX.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                _district.postValue(it)
            }.addTo(compositeDisposable)
    }

    private fun addSubDistrictState() {
        subDistrictRx.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                _subDistrict.postValue(it)
            }.addTo(compositeDisposable)
    }

    fun handleChoosePlace(province: PlaceResult, district: PlaceResult, subDistrict: PlaceResult) {
        when {
            province.id != null && district.id == null && subDistrict.id == null -> {
                provinceRX.onNext(province)
                _txtChoosePlace.value = province.name
            }
            province.id != null && district.id != null && subDistrict.id == null -> {
                provinceRX.onNext(province)
                districtRX.onNext(district)
                _txtChoosePlace.value = province.name + ", " + district.name
            }
            province.id != null && district.id != null && subDistrict.id != null -> {
                provinceRX.onNext(province)
                districtRX.onNext(district)
                subDistrictRx.onNext(subDistrict)
                _txtChoosePlace.value = province.name + ", " + district.name + ", " + subDistrict.name
            }
        }
    }

    fun onClickToSearch() {
        btnClick.onNext(true)
    }

    fun onClickToSearchSuccess() {
        btnClick.onNext(false)
    }

    private fun btnPlaceClick() {
        btnClick.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                btnPlaceClicked(it)
            }.addTo(compositeDisposable)
    }

    private fun btnPlaceClicked(boolean: Boolean) {
        _btnClick.value = boolean
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}