package com.chaitrika.expensemanagement.transactionManagementModule.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionModelDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionModel): Long

    @Query("SELECT * FROM `transactionModel` ORDER BY date DESC")
    fun getAllTransactionsFlow(): Flow<List<TransactionModel>>

    @Query("SELECT * FROM `transactionModel` WHERE title LIKE '%'||:search||'%' OR description LIKE '%'||:search||'%' ORDER BY date DESC")
    fun getAllTransactionsBySearch(search: String): List<TransactionModel>

    @Query("SELECT * FROM `transactionModel` WHERE  t_id IN (:id)")
    suspend fun getTransactionById(id: Long): TransactionModel?

    @Query("DELETE FROM `transactionModel` WHERE t_id IN (:id)")
    fun deleteTransaction(id: Long)

    @Query("Delete from `transactionModel`")
    fun deleteAllTransactions()

}