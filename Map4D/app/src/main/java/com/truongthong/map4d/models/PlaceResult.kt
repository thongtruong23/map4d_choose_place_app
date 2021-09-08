package com.truongthong.map4d.models
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PlaceResult(

    @SerializedName("code")
    val code : String? = null,

    @SerializedName("id")
    val id : String? = null,

    @SerializedName("name")
    val name : String? = null,

    @SerializedName("description")
    val description : String? = null,

    @SerializedName("level")
    val level : Int? = null,

    @SerializedName("type")
    val type : String? = null,

    @SerializedName("parentId")
    val parentId : Int? = null,

    @SerializedName("viewbox")
    val viewbox : String? = null
    ) : Parcelable