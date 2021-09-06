package com.truongthong.map4d.repository

import com.truongthong.map4d.models.Place
import com.truongthong.map4d.models.PlaceResult
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.ReplaySubject

interface PlaceRepository {

    fun getListProvince(compositeDisposable: CompositeDisposable) : ReplaySubject<Resource<Place<MutableList<PlaceResult>>>>

    fun getListDistrict(compositeDisposable: CompositeDisposable, provinceCode : String)
        : ReplaySubject<Resource<Place<MutableList<PlaceResult>>>>

    fun getListSubDistrict(compositeDisposable: CompositeDisposable, districtCode : String)
        : ReplaySubject<Resource<Place<MutableList<PlaceResult>>>>
}