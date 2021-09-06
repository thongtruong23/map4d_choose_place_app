package com.truongthong.map4d.repository

import com.truongthong.map4d.api.PlaceService
import com.truongthong.map4d.models.Place
import com.truongthong.map4d.models.PlaceResult
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.ReplaySubject

class PlaceRepositoryImplement : PlaceRepository {

    override fun getListProvince(compositeDisposable: CompositeDisposable): ReplaySubject<Resource<Place<MutableList<PlaceResult>>>> {
        return object : NetworkBoundResource<Place<MutableList<PlaceResult>>, Place<MutableList<PlaceResult>>>(){
            override fun callApi(): Single<Place<MutableList<PlaceResult>>> = PlaceService.placeApiService.getListProvince()
        }.handleSubject()
    }

    override fun getListDistrict(
        compositeDisposable: CompositeDisposable,
        provinceCode: String
    ): ReplaySubject<Resource<Place<MutableList<PlaceResult>>>> {
        return object : NetworkBoundResource<Place<MutableList<PlaceResult>>, Place<MutableList<PlaceResult>>>(){
            override fun callApi(): Single<Place<MutableList<PlaceResult>>> = PlaceService.placeApiService.getListDistrict(provinceCode)
        }.handleSubject()
    }

    override fun getListSubDistrict(
        compositeDisposable: CompositeDisposable,
        districtCode: String
    ): ReplaySubject<Resource<Place<MutableList<PlaceResult>>>> {
        return object : NetworkBoundResource<Place<MutableList<PlaceResult>>, Place<MutableList<PlaceResult>>>(){
            override fun callApi(): Single<Place<MutableList<PlaceResult>>> = PlaceService.placeApiService.getListSubDistrict(districtCode)
        }.handleSubject()
    }


}