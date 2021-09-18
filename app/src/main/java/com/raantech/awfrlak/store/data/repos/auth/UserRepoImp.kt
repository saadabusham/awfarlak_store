package com.raantech.awfrlak.store.data.repos.auth

import com.raantech.awfrlak.store.data.api.response.APIResource
import com.raantech.awfrlak.store.data.api.response.ResponseHandler
import com.raantech.awfrlak.store.data.api.response.ResponseWrapper
import com.raantech.awfrlak.store.data.daos.remote.user.UserRemoteDao
import com.raantech.awfrlak.store.data.enums.UserEnums
import com.raantech.awfrlak.store.data.models.auth.login.TokenModel
import com.raantech.awfrlak.store.data.models.auth.login.UserDetailsResponseModel
import com.raantech.awfrlak.store.data.pref.user.UserPref
import com.raantech.awfrlak.store.data.repos.base.BaseRepo
import javax.inject.Inject

class UserRepoImp @Inject constructor(
        responseHandler: ResponseHandler,
        private val userRemoteDao: UserRemoteDao,
        private val userPref: UserPref
) : BaseRepo(responseHandler), UserRepo {


    override suspend fun login(
            phoneNumber: String
    ): APIResource<ResponseWrapper<TokenModel>> {
        return try {
            responseHandle.handleSuccess(
                    userRemoteDao.login(
                            phoneNumber
                    )
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }


    override suspend fun logout(): APIResource<ResponseWrapper<Any>> {
        return try {
            responseHandle.handleSuccess(
                    userRemoteDao.logout(
                    )
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun resendCode(token: String): APIResource<ResponseWrapper<TokenModel>> {
        return try {
            responseHandle.handleSuccess(
                    userRemoteDao.resendCode(
                            token
                    )
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun verify(
            token: String,
            code: Int,
            device_token: String,
            platform: String
    ): APIResource<ResponseWrapper<UserDetailsResponseModel>> {
        return try {
            responseHandle.handleSuccess(
                    userRemoteDao.verify(
                            token, code, device_token, platform
                    )
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun register(token: String, name: String, address: String, email: String): APIResource<ResponseWrapper<UserDetailsResponseModel>> {
        return try {
            responseHandle.handleSuccess(
                    userRemoteDao.register(
                            token, name, address, email
                    )
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }


    override fun saveAccessToken(accessToken: String) {
        userPref.saveAccessToken(accessToken)
    }

    override fun getAccessToken(): String {
        return userPref.getAccessToken()
    }

    override fun saveUserPassword(password: String) {
        userPref.saveUserPassword(password)
    }

    override fun getUserPassword(): String {
        return userPref.getUserPassword()
    }

    override fun setUserStatus(userState: UserEnums.UserState) {
        userPref.setUserStatus(userState.ordinal)
    }

    override fun getUserStatus(): UserEnums.UserState {
        return UserEnums.UserState.getUserStateByPosition(userPref.getUserStatus())
    }


    override fun saveNotificationStatus(flag: Boolean) {
        userPref.setNotificationStatus(flag)
    }

    override fun getNotificationStatus(): Boolean {
        return userPref.getNotificationStatus()
    }

    override fun saveTouchIdStatus(flag: Boolean) {
        userPref.setTouchIdStatus(flag)
    }

    override fun getTouchIdStatus(): Boolean {
        return userPref.getTouchIdStatus()
    }

    override fun setUser(user: UserDetailsResponseModel) {
        userPref.setUser(user)
    }

    override fun getUser(): UserDetailsResponseModel? {
        return userPref.getUser()
    }

}