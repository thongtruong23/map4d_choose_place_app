package com.truongthong.map4d.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.truongthong.map4d.models.PlaceResult
import com.truongthong.map4d.ui.TypePlace
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject

class PlaceViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val backClickRX = PublishSubject.create<Boolean>()
    private val _backClick = MutableLiveData<Boolean>()
    val backClick: LiveData<Boolean>
        get() = _backClick

    private val provincePlaceRX = PublishSubject.create<PlaceResult>()
    private val _provincePlace = MutableLiveData<PlaceResult>()
    val provincePlaceResult: LiveData<PlaceResult>
        get() = _provincePlace

    private val districtPlaceRX = PublishSubject.create<PlaceResult>()
    private val _districtPlace = MutableLiveData<PlaceResult>()
    val districtPlaceResult: LiveData<PlaceResult>
        get() = _districtPlace

    private val subDistrictRX = PublishSubject.create<PlaceResult>()
    private val _subDistrictPlace = MutableLiveData<PlaceResult>()
    val subDistrictPlaceResult: LiveData<PlaceResult>
        get() = _subDistrictPlace


    private val stateEdtProvinceRX = PublishSubject.create<Boolean>()
    private val _stateEdtProvince = MutableLiveData<Boolean>()
    val stateEdtProvince: LiveData<Boolean>
        get() = _stateEdtProvince

    private val stateEdtDistrictRX = PublishSubject.create<Boolean>()
    private val _stateEdtDistrict = MutableLiveData<Boolean>()
    val stateEdtDistrict: LiveData<Boolean>
        get() = _stateEdtDistrict

    private val stateEdtSubDistrictRX = PublishSubject.create<Boolean>()
    private val _stateEdtSubDistrict = MutableLiveData<Boolean>()
    val stateEdtSubDistrict: LiveData<Boolean>
        get() = _stateEdtSubDistrict

    private val _provinceCode = MutableLiveData<String>()
    val provinceCode: LiveData<String>
        get() = _provinceCode

    private val _districtCode = MutableLiveData<String>()
    val districtCode: LiveData<String>
        get() = _districtCode

    private val stateBtnRx = PublishSubject.create<Boolean>()
    private val _stateBtn = MutableLiveData<Boolean>()
    val stateBtn: LiveData<Boolean>
        get() = _stateBtn

    init {
        backClick()
        addProvincePlace()
        addDistrictPlace()
        addSubDistrictPlace()
        addEdtProvince()
        addEdtDistrict()
        addStateEdtSubDistrict()
        _stateBtn.value = false
        addStateButton()
    }

    private fun addStateButton() {
        stateBtnRx.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                _stateBtn.postValue(it)
            }.addTo(compositeDisposable)
    }

    fun handleStateButton() {
        if (provincePlaceResult.value != null) {
            stateBtnRx.onNext(true)
        } else {
            stateBtnRx.onNext(false)
        }
    }

    private fun backClick() {
        backClickRX.observeOn(AndroidSchedulers.mainThread())
            .observeOn(Schedulers.io())
            .subscribe {
                _backClick.postValue(it)
            }.addTo(compositeDisposable)
    }

    private fun addProvincePlace() {
        provincePlaceRX.subscribeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                _provincePlace.postValue(it)
            }.addTo(compositeDisposable)
    }

    private fun addDistrictPlace() {
        districtPlaceRX.subscribeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                _districtPlace.postValue(it)
            }.addTo(compositeDisposable)
    }

    private fun addSubDistrictPlace() {
        subDistrictRX.subscribeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                _subDistrictPlace.postValue(it)
            }.addTo(compositeDisposable)
    }

    private fun addEdtProvince() {
        stateEdtProvinceRX.subscribeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                _stateEdtProvince.postValue(it)
            }.addTo(compositeDisposable)
    }

    fun onStateEDTProvinceVisible() {
        stateEdtProvinceRX.onNext(true)
    }

    fun onStateEDTProvinceDisable() {
        stateEdtProvinceRX.onNext(false)
    }

    private fun addEdtDistrict() {
        stateEdtDistrictRX.subscribeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                _stateEdtDistrict.postValue(it)
            }.addTo(compositeDisposable)
    }

    fun onStateEDTDistrictVisible() {
        stateEdtDistrictRX.onNext(true)
    }

    fun onStateEDTDistrictDisable() {
        stateEdtDistrictRX.onNext(false)
    }

    private fun addStateEdtSubDistrict() {
        stateEdtSubDistrictRX.subscribeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                _stateEdtSubDistrict.postValue(it)
            }.addTo(compositeDisposable)
    }

    fun onStateEDTSubDistrictVisible() {
        stateEdtSubDistrictRX.onNext(true)
    }

    fun onStateEDTSubDistrictDisable() {
        stateEdtSubDistrictRX.onNext(false)
    }

    fun onBackClick() {
        backClickRX.onNext(true)
    }

    fun onBackCliked() {
        backClickRX.onNext(false)
    }

    fun handleCountryDetail(typePlace: TypePlace, placeResult: PlaceResult) {
        when (typePlace) {
            TypePlace.PROVINCE -> {
                provincePlaceRX.onNext(placeResult)
                _districtPlace.postValue(null)
                _subDistrictPlace.postValue(null)
                _provinceCode.postValue(placeResult.code)
            }
            TypePlace.DISTRICT -> {
                districtPlaceRX.onNext(placeResult)
                _subDistrictPlace.postValue(null)
                _districtCode.postValue(placeResult.code)
            }
            TypePlace.SUBDISTRICT -> {
                subDistrictRX.onNext(placeResult)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}