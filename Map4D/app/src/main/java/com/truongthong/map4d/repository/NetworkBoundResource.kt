package com.truongthong.map4d.repository

import com.truongthong.map4d.models.Place
import com.truongthong.map4d.models.PlaceResult
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.ReplaySubject

abstract class NetworkBoundResource<ResultType, RequestType> {

    private val compositeDisposable = CompositeDisposable()
    abstract fun callApi(): Observable<Place<MutableList<PlaceResult>>>

    @Suppress("unchecked_cast")
    fun handleSubject(): ReplaySubject<Resource<ResultType>> {
        val replaySubject: ReplaySubject<Resource<ResultType>> = ReplaySubject.create()

        loading(replaySubject)

        try {
            callApi().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe({
                    success(replaySubject, it as ResultType)
                }, {
                    error(replaySubject, it)
                }).addTo(compositeDisposable)
        } catch (e: Exception) {
            error(replaySubject, e)
        }
        return replaySubject
    }

    private fun error(replaySubject: ReplaySubject<Resource<ResultType>>, t: Throwable) {
        replaySubject.onNext(Resource.error(t.message.toString(), null))
    }

    private fun success(
        replaySubject: ReplaySubject<Resource<ResultType>>,
        resultType: ResultType
    ) {
        replaySubject.onNext(Resource.success(resultType))
    }

    private fun loading(replaySubject: ReplaySubject<Resource<ResultType>>) {
        replaySubject.onNext(Resource.loading(null))
    }
}