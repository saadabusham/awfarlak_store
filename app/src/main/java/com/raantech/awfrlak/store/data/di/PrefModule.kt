package com.raantech.awfrlak.store.data.di

import android.content.Context
import com.raantech.awfrlak.store.data.pref.configuration.ConfigurationPref
import com.raantech.awfrlak.store.data.pref.configuration.ConfigurationPrefImp
import com.raantech.awfrlak.store.data.pref.user.UserPref
import com.raantech.awfrlak.store.data.pref.user.UserPrefImp
import com.raantech.awfrlak.store.utils.pref.SharedPreferencesUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object PrefModule {

    @Provides
    @Singleton
    fun provideSharedPreferencesUtil(@ApplicationContext context: Context): SharedPreferencesUtil {
        return SharedPreferencesUtil.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideConfigurationPref(preferencesUtil: SharedPreferencesUtil): ConfigurationPref {
        return ConfigurationPrefImp(preferencesUtil)
    }

    @Provides
    @Singleton
    fun provideUserPref(preferencesUtil: SharedPreferencesUtil): UserPref {
        return UserPrefImp(preferencesUtil)
    }
}