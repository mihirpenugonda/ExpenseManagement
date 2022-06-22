package com.chaitrika.expensemanagement.utils.di

import com.chaitrika.expensemanagement.transactionManagementModule.data.local.TransactionDatabase
import com.chaitrika.expensemanagement.transactionManagementModule.data.repository.ITransactionApiRepository
import com.chaitrika.expensemanagement.transactionManagementModule.domain.TransactionApiRepository
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