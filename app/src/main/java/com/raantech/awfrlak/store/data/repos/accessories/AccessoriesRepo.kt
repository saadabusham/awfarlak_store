package com.raantech.awfrlak.store.data.repos.accessories

import com.raantech.awfrlak.store.data.api.response.APIResource
import com.raantech.awfrlak.store.data.api.response.ResponseWrapper
import com.raantech.awfrlak.store.data.models.GeneralLookup
import com.raantech.awfrlak.store.data.models.MobileRequest
import com.raantech.awfrlak.store.data.models.home.*

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

}