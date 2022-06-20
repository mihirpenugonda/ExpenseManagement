package com.mhirrr.expensemanagement.transactionManagementModule.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [TransactionModel::class],
    version = 1
)

abstract class TransactionDatabase : RoomDatabase() {
    abstract val transactionsDao: TransactionModelDao

}