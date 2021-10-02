package com.raantech.awfrlak.store.data.repos.auth

import com.raantech.awfrlak.store.data.api.response.APIResource
import com.raantech.awfrlak.store.data.api.response.ResponseWrapper
import com.raantech.awfrlak.store.data.enums.UserEnums
import com.raantech.awfrlak.store.data.models.auth.login.TokenModel
import com.raantech.awfrlak.store.data.models.auth.login2.UserDetailsResponseModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*


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
        storeName: RequestBody,
        city: RequestBody,
        longitude: RequestBody,
        latitude: RequestBody,
        commercialRegister: MultipartBody.Part,
        responsible_person: RequestBody,
        logo: MultipartBody.Part,
        description: RequestBody,
        phone_number: RequestBody,
        additionalImages: List<MultipartBody.Part>
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