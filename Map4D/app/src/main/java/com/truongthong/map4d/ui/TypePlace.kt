package com.truongthong.map4d.ui

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class TypePlace(val type : String) : Parcelable {
    PROVINCE("province"),
    DISTRICT("district"),
    SUBDISTRICT("subdistrict")
}