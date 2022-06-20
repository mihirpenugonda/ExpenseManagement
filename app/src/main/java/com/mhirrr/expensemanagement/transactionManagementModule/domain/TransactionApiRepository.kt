package com.mhirrr.expensemanagement.transactionManagementModule.domain

import com.mhirrr.expensemanagement.transactionManagementModule.data.local.TransactionModel
import kotlinx.coroutines.flow.Flow

interface TransactionApiRepository {

    suspend fun insertTransaction(transaction: TransactionModel): Long

    suspend fun getAllTransactions(): Flow<List<TransactionModel>>

    suspend fun getTransactionBySearch(search: String): List<TransactionModel>

    suspend fun getTransactionById(id: Long): TransactionModel?

    suspend fun deleteTransaction(id: Long)

    suspend fun deleteAllTransactions()

}