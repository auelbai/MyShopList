package com.example.myshoplist.di

import android.app.Application
import com.example.myshoplist.data.AppDatabase
import com.example.myshoplist.data.ShopListDao
import com.example.myshoplist.data.ShopListRepositoryImpl
import com.example.myshoplist.domain.ShopListRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @Binds
    @ApplicationScope
    fun bindsRepository(impl: ShopListRepositoryImpl): ShopListRepository

    companion object {
        @Provides
        @ApplicationScope
        fun provideShopListDao(application: Application): ShopListDao {
            return AppDatabase.getInstance(application).shopListDao()
        }
    }
}