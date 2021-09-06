package com.truongthong.map4d.ui

import com.truongthong.map4d.models.PlaceResult


interface PlaceUpdate {
    fun dataListener(typePlace: TypePlace, placeResult: PlaceResult)
}