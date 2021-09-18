package com.raantech.awfrlak.store.data.pref.configuration

interface ConfigurationPref {

    fun setAppLanguageValue(selectedLanguageValue: String)
    fun getAppLanguageValue():String
}