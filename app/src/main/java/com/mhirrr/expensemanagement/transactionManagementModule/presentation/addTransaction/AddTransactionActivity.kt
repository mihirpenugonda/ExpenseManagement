package com.mhirrr.expensemanagement.transactionManagementModule.presentation.addTransaction

import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.mhirrr.expensemanagement.transactionManagementModule.data.local.TransactionModel
import com.mhirrr.expensemanagement.transactionManagementModule.domain.TransactionApiRepository
import com.mhirrr.expensemanagement.utils.dataUtils.DateTimeConverters
import com.mhirrr.expensemanagement.utils.widgetUtils.DatePickerExtender
import com.google.android.material.textfield.TextInputLayout
import com.mhirrr.expensemanagement.R
import com.mhirrr.expensemanagement.databinding.ActivityAddTransactionBinding
import com.mhirrr.expensemanagement.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AddTransactionActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddTransactionBinding

    private val dateTimeConverters = DateTimeConverters()

    @Inject
    lateinit var transactionRepository: TransactionApiRepository

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        DatePickerExtender(this, binding.addTransactionDate)

        val transactionBindings = listOf(
            binding.addTransactionTitle,
            binding.addTransactionDescription,
            binding.addTransactionDate,
            binding.addTransactionAmount,
            binding.addTransactionCategory
        )

        transactionBindings.forEach { input ->
            val parent = input.parent.parent as TextInputLayout
            input.doOnTextChanged { _, _, _, _ ->
                parent.isErrorEnabled = false
            }
        }

        val categoryAdapter: ArrayAdapter<String> = ArrayAdapter(
            applicationContext,
            R.layout.rv_dropdown_item,
            Constants.transactionCategory
        )
        binding.addTransactionCategory.setAdapter(categoryAdapter)

        binding.addTransactionSubmitButton.setOnClickListener { handleSubmit(transactionBindings) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleSubmit(transactionBindings: List<EditText>) {
        var valid = true

        transactionBindings.forEach { input ->
            val parent = input.parent.parent as TextInputLayout
            if (input.text.toString() == "") {
                parent.error = "${input.hint} can't be empty"
                parent.isErrorEnabled = true
                valid = false
            }
        }

        val transactionTabSelected = binding.addTransactionTab.selectedTabPosition
        val transactionType = if (transactionTabSelected == 0) {
            "income"
        } else {
            "expense"
        }

        if (valid) {
            val newTransaction = TransactionModel(
                binding.addTransactionTitle.text.toString(),
                binding.addTransactionDescription.text.toString(),
                binding.addTransactionAmount.text.toString(),
                dateTimeConverters.stringDateToLong(binding.addTransactionDate.text.toString()),
                binding.addTransactionCategory.text.toString(),
                transactionType,
            )
            lifecycleScope.launch {
                transactionRepository.insertTransaction(newTransaction)
                finish()
            }
        } else {
            Toast.makeText(applicationContext, "please fill all details", Toast.LENGTH_SHORT)
                .show()
        }
    }
}