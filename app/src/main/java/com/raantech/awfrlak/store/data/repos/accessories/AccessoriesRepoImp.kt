package com.raantech.awfrlak.store.data.repos.accessories

import com.raantech.awfrlak.store.data.api.response.APIResource
import com.raantech.awfrlak.store.data.api.response.ResponseHandler
import com.raantech.awfrlak.store.data.api.response.ResponseWrapper
import com.raantech.awfrlak.store.data.daos.remote.accessories.AccessoriesRemoteDao
import com.raantech.awfrlak.store.data.models.home.*
import com.raantech.awfrlak.store.data.repos.base.BaseRepo
import javax.inject.Inject

class AccessoriesRepoImp @Inject constructor(
    responseHandler: ResponseHandler,
    private val accessoriesRemoteDao: AccessoriesRemoteDao
) : BaseRepo(responseHandler), AccessoriesRepo {
    override suspend fun getHome(): APIResource<ResponseWrapper<HomeResponse>> {
        return try {
            responseHandle.handleSuccess(accessoriesRemoteDao.getHome())
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun getMobiles(
        skip: Int,
        store_id: Int?,
        search_text: String?
    ): APIResource<ResponseWrapper<List<MobilesItem>>> {
        return try {
            responseHandle.handleSuccess(
                accessoriesRemoteDao.getMobiles(
                    skip,
                    store_id,
                    search_text
                )
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun getAccessories(
        skip: Int,
        store_id: Int?,
        search_text: String?
    ): APIResource<ResponseWrapper<List<AccessoriesItem>>> {
        return try {
            responseHandle.handleSuccess(
                accessoriesRemoteDao.getAccessories(
                    skip,
                    store_id,
                    search_text
                )
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun getStores(
        skip: Int,
        search_text: String?
    ): APIResource<ResponseWrapper<List<Store>>> {
        return try {
            responseHandle.handleSuccess(accessoriesRemoteDao.getStores(skip,search_text))
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun getServices(
        skip: Int,
        store_id: Int?,
        search_text: String?
    ): APIResource<ResponseWrapper<List<Service>>> {
        return try {
            responseHandle.handleSuccess(
                accessoriesRemoteDao.getServices(
                    skip,
                    store_id,
                    search_text
                )
            )
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun getAccessory(id: Int): APIResource<ResponseWrapper<AccessoriesItem>> {
        return try {
            responseHandle.handleSuccess(accessoriesRemoteDao.getAccessory(id))
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

}