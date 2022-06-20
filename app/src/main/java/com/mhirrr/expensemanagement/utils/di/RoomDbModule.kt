package com.mhirrr.expensemanagement.utils.di

import android.content.Context
import androidx.room.Room
import com.mhirrr.expensemanagement.transactionManagementModule.data.local.TransactionDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RoomDbModule {

    @Singleton
    @Provides
    fun provideTransactionDatabase(@ApplicationContext applicationContext: Context): TransactionDatabase {
        return Room.databaseBuilder(
            applicationContext,
            TransactionDatabase::class.java,
            "transactions"
        ).build()
    }

}