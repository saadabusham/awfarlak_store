package com.raantech.awfrlak.store.data.di


import com.raantech.awfrlak.store.data.repos.accessories.AccessoriesRepo
import com.raantech.awfrlak.store.data.repos.accessories.AccessoriesRepoImp
import com.raantech.awfrlak.store.data.repos.auth.UserRepo
import com.raantech.awfrlak.store.data.repos.auth.UserRepoImp
import com.raantech.awfrlak.store.data.repos.configuration.ConfigurationRepo
import com.raantech.awfrlak.store.data.repos.configuration.ConfigurationRepoImp
import com.raantech.awfrlak.store.data.repos.media.MediaRepo
import com.raantech.awfrlak.store.data.repos.media.MediaRepoImp
import com.raantech.awfrlak.store.data.repos.orders.OrdersRepo
import com.raantech.awfrlak.store.data.repos.orders.OrdersRepoImp
import com.raantech.awfrlak.store.data.repos.wishlist.WishListRepo
import com.raantech.awfrlak.store.data.repos.wishlist.WishListRepoImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {

    @Singleton
    @Binds
    abstract fun bindConfigurationRepo(configurationRepoImp: ConfigurationRepoImp): ConfigurationRepo

    @Singleton
    @Binds
    abstract fun bindUserRepo(userRepoImp: UserRepoImp) : UserRepo

    @Singleton
    @Binds
    abstract fun bindMediaRepo(mediaRepoImp: MediaRepoImp): MediaRepo

    @Singleton
    @Binds
    abstract fun bindAccessoryRepo(accessoriesRepoImp: AccessoriesRepoImp): AccessoriesRepo

    @Singleton
    @Binds
    abstract fun bindWishListRepo(wishListRepoImp: WishListRepoImp): WishListRepo

    @Singleton
    @Binds
    abstract fun bindOrdersRepo(ordersRepoImp: OrdersRepoImp): OrdersRepo

}