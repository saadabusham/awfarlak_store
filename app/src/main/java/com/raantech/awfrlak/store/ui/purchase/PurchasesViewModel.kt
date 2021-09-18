package com.raantech.awfrlak.store.ui.purchase

import androidx.lifecycle.SavedStateHandle
import com.raantech.awfrlak.store.data.repos.configuration.ConfigurationRepo
import com.raantech.awfrlak.store.ui.base.viewmodel.BaseViewModel
import com.raantech.awfrlak.store.utils.pref.SharedPreferencesUtil
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PurchasesViewModel @Inject constructor(
        private val configurationRepo: ConfigurationRepo,
        private val sharedPreferencesUtil: SharedPreferencesUtil
) : BaseViewModel() {


    fun getPhoneNumber(): String? {
        return sharedPreferencesUtil.getConfigurationPreferences().appPhoneNumber
    }

}