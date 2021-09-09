package com.truongthong.map4d.utils


import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.truongthong.map4d.models.PlaceResult

@BindingAdapter("textPlace")
fun textPlace(textView: TextView, placeResult: PlaceResult?) {
    when (placeResult) {
        null -> {
            textView.text = ""
        }
        else -> {
            textView.text = placeResult.name
        }
    }
}

