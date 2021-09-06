package com.truongthong.map4d.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.truongthong.map4d.models.Place
import com.truongthong.map4d.models.PlaceResult
import com.truongthong.map4d.repository.PlaceRepository
import com.truongthong.map4d.repository.PlaceRepositoryImplement
import com.truongthong.map4d.repository.Resource
import com.truongthong.map4d.ui.TypePlace
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.lang.Exception

class DialogPlaceViewModel : ViewModel() {

    private val _provinceCode = MutableLiveData<String>()
    private val _districtCode = MutableLiveData<String>()
    private val btnCloseClick = PublishSubject.create<Boolean>()
    private val compositeDisposable = CompositeDisposable()
    private val placeRepository: PlaceRepository = PlaceRepositoryImplement()
    private var placeType = PublishSubject.create<TypePlace>()

    private val _btnCloseClick = MutableLiveData<Boolean>()
    val btnCloseState: LiveData<Boolean>
        get() = _btnCloseClick

    private val _placeResult = MutableLiveData<MutableList<PlaceResult>>()
    val placeResult: LiveData<MutableList<PlaceResult>>
        get() = _placeResult

    private val _type = MutableLiveData<TypePlace>()
    val typePlace: LiveData<TypePlace>
        get() = _type

    init {
        handleTypePlace()
        closeButtonClick()
    }

    private fun getListProvince() {
        placeRepository.getListProvince(compositeDisposable)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                handleProvince(it)
            }.addTo(compositeDisposable)
    }

    private fun handleProvince(resource: Resource<Place<MutableList<PlaceResult>>>) {
        val placeResult: Place<MutableList<PlaceResult>>? = resource.data
        println("status: " + resource.status)
        when {
            placeResult?.result != null -> {
                _placeResult.value = placeResult.result
            }
            else -> {
                _placeResult.value = null
            }
        }
    }

    private fun getListDistrict(provinceCode: String) {
        placeRepository.getListDistrict(compositeDisposable, provinceCode)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                handleDistrict(it)
            }.addTo(compositeDisposable)
    }

    private fun handleDistrict(resource: Resource<Place<MutableList<PlaceResult>>>) {
        val districts: Place<MutableList<PlaceResult>>? = resource.data
        when {
            districts?.result != null -> {
                _placeResult.value = districts.result
            }
            else -> {
                _placeResult.value = null
            }
        }
    }

    fun getListSubDistrict(districtCode: String) {
        placeRepository.getListSubDistrict(compositeDisposable, districtCode)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                handleSubDistrict(it)
            }.addTo(compositeDisposable)
    }

    private fun handleSubDistrict(resource: Resource<Place<MutableList<PlaceResult>>>) {
        val subDistricts: Place<MutableList<PlaceResult>>? = resource.data
        when {
            subDistricts?.result != null -> {
                _placeResult.value = subDistricts.result
            }
            else -> {
                _placeResult.value = null
            }
        }
    }

    private fun handleTypePlace() {
        try {
            placeType.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe {
                    //println("type: $it")
                    _type.value = it
                    handleTypePlace(it.type)
                }.addTo(compositeDisposable)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun handleTypePlace(type: String) {
        when (type) {
            TypePlace.PROVINCE.type -> {
                this.getListProvince()
            }
            TypePlace.DISTRICT.type -> {
                _provinceCode.value?.let { getListDistrict(it) }
            }
            TypePlace.SUBDISTRICT.type -> {
                _districtCode.value?.let {
                    getListSubDistrict(it)
                }
            }
        }
    }

    private fun closeButtonClick() {
        btnCloseClick.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                _btnCloseClick.value = it
            }.addTo(compositeDisposable)
    }

    fun onCloseButtonClicked() {
        btnCloseClick.onNext(true)
    }

    fun onCloseButtonClickSuccess() {
        btnCloseClick.onNext(false)

    }

    fun setType(typePlace: TypePlace, code: String) {
        when (typePlace) {
            TypePlace.PROVINCE -> {

            }
            TypePlace.DISTRICT -> {
                _provinceCode.value = code
            }
            TypePlace.SUBDISTRICT -> _districtCode.value = code
        }
        placeType.onNext(typePlace)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}