package com.raantech.awfrlak.store.data.repos.cart.mobilecart

import androidx.lifecycle.LiveData
import com.raantech.awfrlak.store.data.api.response.ResponseHandler
import com.raantech.awfrlak.store.data.daos.local.cart.MobileCartLocalDao
import com.raantech.awfrlak.store.data.models.home.MobilesItem
import com.raantech.awfrlak.store.data.repos.base.BaseRepo
import javax.inject.Inject

class MobileCartRepoImp @Inject constructor(
    responseHandler: ResponseHandler,
    private val cartLocalDao: MobileCartLocalDao
) : BaseRepo(responseHandler), MobileCartRepo {

    override suspend fun saveCart(mobile: MobilesItem) {
        cartLocalDao.saveCart(mobile)
    }

    override suspend fun saveCarts(mobiles: List<MobilesItem>) {
        cartLocalDao.saveCarts(mobiles)
    }

    override suspend fun loadCarts(): List<MobilesItem> {
        return cartLocalDao.getCarts()
    }

    override suspend fun getCart(id: Int): MobilesItem {
        return cartLocalDao.getCart(id)
    }

    override fun getCartsCount(): LiveData<Int> {
        return cartLocalDao.getCartsCount()
    }

    override suspend fun getCartsCountInt(): Int? {
        return cartLocalDao.getCartsCountInt()
    }

    override suspend fun deleteCart(id: Int) {
        cartLocalDao.deleteCart(id)
    }

    override suspend fun clearCart() {
        cartLocalDao.clearCart()
    }


}