package com.raantech.awfrlak.store.data.repos.accessories

import com.raantech.awfrlak.store.data.api.response.APIResource
import com.raantech.awfrlak.store.data.api.response.ResponseWrapper
import com.raantech.awfrlak.store.data.common.NetworkConstants
import com.raantech.awfrlak.store.data.models.AccessoryRequest
import com.raantech.awfrlak.store.data.models.GeneralLookup
import com.raantech.awfrlak.store.data.models.MobileRequest
import com.raantech.awfrlak.store.data.models.ServiceRequest
import com.raantech.awfrlak.store.data.models.home.*
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.PUT
import retrofit2.http.Path

interface AccessoriesRepo {

    suspend fun getHome(
    ): APIResource<ResponseWrapper<HomeResponse>>

    suspend fun getMobiles(
            skip: Int
    ): APIResource<ResponseWrapper<List<MobilesItem>>>


    suspend fun getAccessories(
            skip: Int
    ): APIResource<ResponseWrapper<List<AccessoriesItem>>>

    suspend fun getStores(
            skip: Int
    ): APIResource<ResponseWrapper<List<Store>>>

    suspend fun getServices(
            skip: Int
    ): APIResource<ResponseWrapper<List<Service>>>

    suspend fun getAccessory(
            id: Int
    ): APIResource<ResponseWrapper<AccessoriesItem>>

    suspend fun addMobile(
            mobileRequest: MobileRequest
    ): APIResource<ResponseWrapper<MobilesItem>>

    suspend fun updateMobile(
            id: Int,
            mobileRequest: MobileRequest
    ): APIResource<ResponseWrapper<MobilesItem>>

    suspend fun deleteMobile(
            id: Int
    ): APIResource<ResponseWrapper<MobilesItem>>

    suspend fun addAccessory(
            accessoryRequest: AccessoryRequest
    ): APIResource<ResponseWrapper<AccessoriesItem>>

    suspend fun updateAccessory(
            id: Int,
            accessoryRequest: AccessoryRequest
    ): APIResource<ResponseWrapper<AccessoriesItem>>

    suspend fun deleteAccessory(
            id: Int
    ): APIResource<ResponseWrapper<AccessoriesItem>>

    suspend fun addService(
            serviceRequest: ServiceRequest
    ): APIResource<ResponseWrapper<AccessoriesItem>>

    suspend fun updateService(
            id: Int,
            serviceRequest: ServiceRequest
    ): APIResource<ResponseWrapper<AccessoriesItem>>

    suspend fun deleteService(
            id: Int
    ): APIResource<ResponseWrapper<AccessoriesItem>>
}