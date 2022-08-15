package com.raantech.awfrlak.store.ui.base.bindingadapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.raantech.awfrlak.R
import com.raantech.awfrlak.store.utils.extensions.gone
import com.rilixtech.widget.countrycodepicker.CountryCodePicker

@BindingAdapter("ccp_selected_country_code_iso2")
fun CountryCodePicker?.setSelectedCountryCodeUsingISO2Name(iso2Name:String?){
    if(iso2Name.isNullOrEmpty()) return
    this?.setCountryForNameCode(iso2Name)
}
@BindingAdapter("ccp_hide_arrow")
fun CountryCodePicker?.hideArrow(hide:Boolean){
    this?.findViewById<ImageView>(R.id.arrow_imv)?.gone()
}