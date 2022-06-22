package com.chaitrika.expensemanagement.transactionManagementModule.data.repository

import com.chaitrika.expensemanagement.transactionManagementModule.data.local.TransactionModel
import com.chaitrika.expensemanagement.transactionManagementModule.data.local.TransactionModelDao
import com.chaitrika.expensemanagement.transactionManagementModule.domain.TransactionApiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ITransactionApiRepository @Inject constructor(
    private val transactionModelDao: TransactionModelDao,
) : TransactionApiRepository {
    override suspend fun insertTransaction(transaction: TransactionModel): Long {
        return transactionModelDao.insertTransaction(transaction)
    }

    override suspend fun getAllTransactions(): Flow<List<TransactionModel>> {
        return transactionModelDao.getAllTransactionsFlow()
    }

    override suspend fun getTransactionBySearch(search: String): List<TransactionModel> {
        return transactionModelDao.getAllTransactionsBySearch(search)
    }

    override suspend fun getTransactionById(id: Long): TransactionModel? {
        return transactionModelDao.getTransactionById(id)
    }

    override suspend fun deleteTransaction(id: Long) {
        transactionModelDao.deleteTransaction(id)
    }

    override suspend fun deleteAllTransactions() {
        transactionModelDao.deleteAllTransactions()
    }
}