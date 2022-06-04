package com.raantech.awfrlak.store.ui.profile.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.raantech.awfrlak.store.data.api.response.APIResource
import com.raantech.awfrlak.store.data.models.auth.login2.UserDetailsResponseModel
import com.raantech.awfrlak.store.data.models.auth.login2.UserInfo
import com.raantech.awfrlak.store.data.repos.auth.UserRepo
import com.raantech.awfrlak.store.ui.base.viewmodel.BaseViewModel
import com.raantech.awfrlak.store.utils.pref.SharedPreferencesUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class UpdateProfileViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val sharedPreferencesUtil: SharedPreferencesUtil,
    @ApplicationContext context: Context
) : BaseViewModel() {

    //    register
    val username: MutableLiveData<String> = MutableLiveData()
    val phoneNumber: MutableLiveData<String> = MutableLiveData()
    val email: MutableLiveData<String> = MutableLiveData()

    init {
        userRepo.getUser()?.userInfo?.let {
            username.postValue(it.name)
            phoneNumber.postValue(it.phoneNumber)
            email.postValue(it.email)
        }
    }
    fun updateUser() = liveData {
        emit(APIResource.loading())
        val response = userRepo.updateProfile(
            username.value.toString(),
            email.value.toString()
        )
        emit(response)
    }

    fun storeUser(userInfo: UserInfo) {
        val user = userRepo.getUser()
        user?.userInfo = userInfo
        user?.let { userRepo.setUser(it) }
    }
}