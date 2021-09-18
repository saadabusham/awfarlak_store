package com.raantech.awfrlak.store.ui.splash

import androidx.lifecycle.liveData
import com.raantech.awfrlak.store.data.api.response.APIResource
import com.raantech.awfrlak.store.data.enums.UserEnums
import com.raantech.awfrlak.store.data.repos.auth.UserRepo
import com.raantech.awfrlak.store.data.repos.configuration.ConfigurationRepo
import com.raantech.awfrlak.store.ui.base.viewmodel.BaseViewModel
import com.raantech.awfrlak.store.utils.pref.SharedPreferencesUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val configurationRepo: ConfigurationRepo,
    private val sharedPreferencesUtil: SharedPreferencesUtil
) : BaseViewModel() {

    fun getConfigurationData() = liveData {
        emit(APIResource.loading())
        val response = configurationRepo.loadConfigurationData()
        emit(response)
    }
//    fun updateAccessToken() {
//        updateTokenResult.postValue(Result.Loading)
//        compositeDisposable + userRepo.updateAccessToken(getAccessToken())
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                if (it.success) {
//                    storeUser(it.data!!)
//                    it.data?.token?.let { it1 -> userRepo.saveAccessToken(it1) }
//                    updateTokenResult.postValue(ResponseSubErrorsCodeEnum.Success(it))
//                } else {
//                    updateTokenResult.postValue(
//                        Result.CustomError(
//                            errorCode = it.code,
//                            message = it.message
//                        )
//                    )
//                }
//            }, {
//                updateTokenResult.postValue(Result.Error(it))
//            })
//
//    }
//    fun storeUser(user: UserDetailsResponseModel) {
//        user.id?.let {
//            userRepo.setUserId(it)
//        }
//        user.token?.let { userRepo.saveAccessToken(it) }
//        userRepo.setUserStatus(UserEnums.UserState.LoggedIn)
//        userRepo.setUser(user)
//    }
//
//    fun getAccessToken(): String {
//        return userRepo.getUser()?.refreshToken?.refreshToken ?: ""
//    }
//
//
//    fun logout() {
//        if (userRepo.getTouchIdStatus())
//            sharedPreferencesUtil.logout()
//        else
//            sharedPreferencesUtil.clearPreference()
//    }

    fun isUserLoggedIn() = userRepo.getUserStatus() == UserEnums.UserState.LoggedIn
}