package com.raantech.awfrlak.store.ui.base.bindingadapters

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.raantech.awfrlak.store.common.CommonEnums
import com.raantech.awfrlak.store.utils.LocaleUtil

@BindingAdapter("drawable_start")
fun TextView.setRelationshipText(
        res: Int
) {
    if (LocaleUtil.getLanguage() == CommonEnums.Languages.Arabic.value)
        setCompoundDrawablesWithIntrinsicBounds(0, 0, res, 0)
    else
        setCompoundDrawablesWithIntrinsicBounds(res, 0, 0, 0)
}