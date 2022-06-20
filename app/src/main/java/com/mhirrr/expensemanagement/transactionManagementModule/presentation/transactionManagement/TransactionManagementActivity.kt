package com.mhirrr.expensemanagement.transactionManagementModule.presentation.transactionManagement

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.mhirrr.expensemanagement.R
import com.mhirrr.expensemanagement.databinding.ActivityTransactionManagementBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionManagementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTransactionManagementBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTransactionManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.transaction_management_fragment)
        navController.setGraph(R.navigation.transaction_management_flow_nav)
    }
}