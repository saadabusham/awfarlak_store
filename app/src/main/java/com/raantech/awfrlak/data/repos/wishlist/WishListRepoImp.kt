package com.raantech.awfrlak.data.repos.wishlist


import com.raantech.awfrlak.data.api.response.APIResource
import com.raantech.awfrlak.data.api.response.ResponseHandler
import com.raantech.awfrlak.data.api.response.ResponseWrapper
import com.raantech.awfrlak.data.daos.remote.wishlist.WishListRemoteDao
import com.raantech.awfrlak.data.models.wishlist.WishList
import com.raantech.awfrlak.data.repos.base.BaseRepo
import javax.inject.Inject

class WishListRepoImp @Inject constructor(
    responseHandler: ResponseHandler,
    private val wishListRemoteDao: WishListRemoteDao
) : BaseRepo(responseHandler), WishListRepo {

    override suspend fun getWishList(skip: Int): APIResource<ResponseWrapper<List<WishList>>> {
        return try {
            responseHandle.handleSuccess(wishListRemoteDao.getWishList(skip))
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun addToWishList(
        entity_type: String,
        entity_id: Int
    ): APIResource<ResponseWrapper<Any>> {
        return try {
            responseHandle.handleSuccess(wishListRemoteDao.addToWishList(entity_type, entity_id))
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun removeFromWishList(
        productId: Int
    ): APIResource<ResponseWrapper<Any>> {
        return try {
            responseHandle.handleSuccess(wishListRemoteDao.removeFromWishList(productId))
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

}