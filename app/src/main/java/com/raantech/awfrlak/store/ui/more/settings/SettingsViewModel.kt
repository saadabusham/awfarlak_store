package com.raantech.awfrlak.store.ui.more.settings

import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import co.infinum.goldfinger.Goldfinger
import com.raantech.awfrlak.R
import com.raantech.awfrlak.store.common.CommonEnums
import com.raantech.awfrlak.store.data.enums.UserEnums
import com.raantech.awfrlak.store.data.pref.configuration.ConfigurationPref
import com.raantech.awfrlak.store.data.repos.auth.UserRepo
import com.raantech.awfrlak.store.ui.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
        val configurationPref: ConfigurationPref,
        val userRepo: UserRepo,
        @ApplicationContext val context: Context
) : BaseViewModel() {

    val englishSelected: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(true) }

    fun getIsNotificationsIsEnabled(): Boolean? {
        return userRepo.getNotificationStatus()
    }

    fun setIsNotificationsIsEnabled(enabled: Boolean) {
        return userRepo.saveNotificationStatus(enabled)
    }

    fun getAppLanguage(): String? {
        return if (configurationPref.getAppLanguageValue() == CommonEnums.Languages.English.value) {
            englishSelected.postValue(true)
            context.resources.getString(R.string.english)
        } else {
            englishSelected.postValue(false)
            context.resources.getString(R.string.arabic)
        }
    }

    fun saveLanguage() = liveData {
        configurationPref.setAppLanguageValue(if (englishSelected.value!!)
            CommonEnums.Languages.Arabic.value
        else CommonEnums.Languages.English.value)
        englishSelected.value = !englishSelected.value!!
        emit(null)
    }

    fun getIsTouchIDEnabled(): Boolean? {
        return userRepo.getTouchIdStatus()
    }

    fun setIsTouchIDEnabled(enabled: Boolean) {
        return userRepo.saveTouchIdStatus(enabled)
    }

    fun isTouchIdShouldVisible():Int{
        return if(userRepo.getUserStatus() == UserEnums.UserState.LoggedIn &&
                Goldfinger.Builder(context).build().canAuthenticate())
            View.VISIBLE
        else View.GONE
    }
}