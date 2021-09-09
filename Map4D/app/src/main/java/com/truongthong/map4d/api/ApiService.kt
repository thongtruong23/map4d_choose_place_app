package com.truongthong.map4d.api

import com.truongthong.map4d.models.Place
import com.truongthong.map4d.models.PlaceResult
import com.truongthong.map4d.utils.Constant
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.core.Observable
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceApiService{

    @GET("map/country/province/all")
    fun getListProvince() : Observable<Place<MutableList<PlaceResult>>>

    @GET("map/country/district")
    fun getListDistrict(@Query("provinceCode") provinceCode : String) : Observable<Place<MutableList<PlaceResult>>>

    @GET("map/country/subdistrict")
    fun getListSubDistrict(@Query("districtCode") districtCode : String) : Observable<Place<MutableList<PlaceResult>>>
}

object PlaceService{

    private val retrofit = Retrofit.Builder()
        .baseUrl(Constant.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()

    val placeApiService: PlaceApiService = retrofit.create(PlaceApiService::class.java)
}