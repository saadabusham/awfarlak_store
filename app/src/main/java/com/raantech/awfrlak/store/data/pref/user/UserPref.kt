package com.raantech.awfrlak.store.data.pref.user

import com.raantech.awfrlak.store.data.models.auth.login.UserDetailsResponseModel

interface UserPref {

    fun setIsFirstOpen(isFirstOpen: Boolean)
    fun getIsFirstOpen(): Boolean

    fun saveAccessToken(accessToken: String)
    fun getAccessToken(): String


    fun saveUserPassword(password: String)
    fun getUserPassword(): String

    fun setNotificationStatus(flag: Boolean)
    fun getNotificationStatus(): Boolean

    fun setTouchIdStatus(flag: Boolean)
    fun getTouchIdStatus(): Boolean

    fun setUserStatus(ordinal: Int)
    fun getUserStatus(): Int

    fun getUser(): UserDetailsResponseModel?

    fun setUser(value: UserDetailsResponseModel?)

    fun logout()
}