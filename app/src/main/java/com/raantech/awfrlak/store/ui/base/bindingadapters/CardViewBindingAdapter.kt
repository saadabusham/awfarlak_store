package com.raantech.awfrlak.store.ui.base.bindingadapters

import android.graphics.Color
import androidx.annotation.DrawableRes
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import com.google.android.material.card.MaterialCardView


@BindingAdapter("cvBackgroundRes")
fun CardView?.setBackgroundRes(@DrawableRes background: Int) {
    this?.setBackgroundResource(background)
}

@BindingAdapter("cvIsChecked")
fun MaterialCardView?.setIsChecked(isChecked: Boolean) {
    this?.isChecked = isChecked
}

@BindingAdapter("cvBackgroundHex")
fun MaterialCardView?.setBackgroundHex(color: String?) {
    if (color.isNullOrEmpty())
        return
    try {
        this?.setCardBackgroundColor(Color.parseColor(color))
    }catch (e :Exception){

    }
}