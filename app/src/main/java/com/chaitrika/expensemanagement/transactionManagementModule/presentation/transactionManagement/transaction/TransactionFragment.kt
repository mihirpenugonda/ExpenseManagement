package com.chaitrika.expensemanagement.transactionManagementModule.presentation.transactionManagement.transaction

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.chaitrika.expensemanagement.transactionManagementModule.data.local.TransactionModel
import com.chaitrika.expensemanagement.utils.dataUtils.DateTimeConverters
import com.chaitrika.expensemanagement.utils.widgetUtils.BaseFragment
import com.chaitrika.expensemanagement.utils.widgetUtils.DatePickerExtender
import com.chaitrika.expensemanagement.utils.Constants
import com.chaitrika.expensemanagement.R
import com.chaitrika.expensemanagement.databinding.FragmentTransactionBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TransactionFragment : BaseFragment<FragmentTransactionBinding>() {

    private val transactionViewModel: TransactionViewModel by viewModels()

    private val dateTimeConverters = DateTimeConverters()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTransactionBinding {
        return FragmentTransactionBinding.inflate(layoutInflater, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = arguments?.getInt("id")

        DatePickerExtender(this.requireContext(), binding.transactionDate)

        val categoryAdapter: ArrayAdapter<String> = ArrayAdapter(
            this.requireContext(),
            R.layout.rv_dropdown_item,
            Constants.transactionCategory.filter { it != "all" }
        )

        binding.transactionCategory.isSelected = true
        binding.transactionCategory.setAdapter(categoryAdapter)

        lifecycleScope.launch {
            transactionViewModel.getTransaction(id!!)
        }

        binding.transactionCategory.setOnClickListener {
            (it as AutoCompleteTextView).showDropdown(categoryAdapter)
        }

        lifecycleScope.launchWhenStarted {
            transactionViewModel.transaction.collect {
                val transaction: TransactionModel? = it.data

                if (transaction != null) {
                    setupViews(transaction)
                }
            }
        }

        binding.transactionUpdateButton.setOnClickListener {
            handleSubmit(transactionViewModel.transaction.value.data!!)
        }
    }

    private fun setupViews(transaction: TransactionModel) {
        with(binding) {
            transactionTitle.isTextInputLayoutFocusedRectEnabled = true
            transactionTitle.setText(transaction.title)
            transactionDescription.setText(transaction.description)
            transactionAmount.setText(transaction.amount)
            transactionDate.setText(dateTimeConverters.longToStringDate(transaction.date))
            transactionCategory.setText(transaction.category)

            if (transaction.type == "expense") transactionTab.getTabAt(1)?.select()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleSubmit(transaction: TransactionModel) {
        val transactionTabSelected = binding.transactionTab.selectedTabPosition
        val transactionType = if (transactionTabSelected == 0) {
            "income"
        } else {
            "expense"
        }

        val newTransaction = TransactionModel(
            binding.transactionTitle.text.toString(),
            binding.transactionDescription.text.toString(),
            binding.transactionAmount.text.toString(),
            dateTimeConverters.stringDateToLong(binding.transactionDate.text.toString()),
            binding.transactionCategory.text.toString(),
            transactionType,
            transaction.frequency,
            transaction.created_at,
            transaction.id
        )

        lifecycleScope.launch {
            transactionViewModel.insertTransaction(newTransaction)
        }
    }
}

fun AutoCompleteTextView.showDropdown(adapter: ArrayAdapter<String>?) {
    if (!TextUtils.isEmpty(this.text.toString())) {
        adapter?.filter?.filter(null)
    }
}
