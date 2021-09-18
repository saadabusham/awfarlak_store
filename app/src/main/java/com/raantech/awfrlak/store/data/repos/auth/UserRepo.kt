package com.raantech.awfrlak.store.data.repos.auth

import com.raantech.awfrlak.store.data.api.response.APIResource
import com.raantech.awfrlak.store.data.api.response.ResponseWrapper
import com.raantech.awfrlak.store.data.common.NetworkConstants
import com.raantech.awfrlak.store.data.enums.UserEnums
import com.raantech.awfrlak.store.data.models.auth.login.TokenModel
import com.raantech.awfrlak.store.data.models.auth.login.UserDetailsResponseModel
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST


interface UserRepo {


    suspend fun login(
        phoneNumber: String
    ): APIResource<ResponseWrapper<TokenModel>>

    suspend fun logout(
    ): APIResource<ResponseWrapper<Any>>

    suspend fun resendCode(
        token: String
    ): APIResource<ResponseWrapper<TokenModel>>

    suspend fun verify(
        token: String,
        code: Int,
        device_token: String,
        platform: String
    ): APIResource<ResponseWrapper<UserDetailsResponseModel>>

    suspend fun register(
            token: String,
            name: String,
            address: String,
            email: String
    ): APIResource<ResponseWrapper<UserDetailsResponseModel>>

    fun saveNotificationStatus(flag: Boolean)
    fun getNotificationStatus(): Boolean

    fun saveTouchIdStatus(flag: Boolean)
    fun getTouchIdStatus(): Boolean

    fun saveAccessToken(accessToken: String)
    fun getAccessToken(): String

    fun saveUserPassword(password: String)
    fun getUserPassword(): String

    fun setUserStatus(userState: UserEnums.UserState)
    fun getUserStatus(): UserEnums.UserState

    fun setUser(user: UserDetailsResponseModel)
    fun getUser(): UserDetailsResponseModel?
}