package com.truongthong.map4d.models

import com.google.gson.annotations.SerializedName

data class Place<T>(

    @SerializedName("code")
    val code : String,

    @SerializedName("message")
    val message : String,

    @SerializedName("result")
    val result : T
)