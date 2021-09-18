package com.raantech.awfrlak.store.data.di.daos

import com.raantech.awfrlak.store.data.daos.local.cart.CartLocalDao
import com.raantech.awfrlak.store.data.daos.local.cart.MobileCartLocalDao
import com.raantech.awfrlak.store.data.db.ApplicationDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDaosModule {

    @Provides
    @Singleton
    fun provideCartLocalDao(
        applicationDB: ApplicationDB
    ): CartLocalDao {
        return applicationDB.cartLocalDao()
    }
    @Provides
    @Singleton
    fun provideMobileCartLocalDao(
        applicationDB: ApplicationDB
    ): MobileCartLocalDao {
        return applicationDB.mobileCartLocalDao()
    }

}