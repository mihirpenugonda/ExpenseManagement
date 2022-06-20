package com.mhirrr.expensemanagement.utils.di

import com.mhirrr.expensemanagement.transactionManagementModule.data.local.TransactionDatabase
import com.mhirrr.expensemanagement.transactionManagementModule.data.repository.ITransactionApiRepository
import com.mhirrr.expensemanagement.transactionManagementModule.domain.TransactionApiRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoriesModule {
    @Singleton
    @Provides
    fun provideTransactionRepository(
        transactionDatabase: TransactionDatabase
    ): TransactionApiRepository {
        return ITransactionApiRepository(transactionDatabase.transactionsDao)
    }
}