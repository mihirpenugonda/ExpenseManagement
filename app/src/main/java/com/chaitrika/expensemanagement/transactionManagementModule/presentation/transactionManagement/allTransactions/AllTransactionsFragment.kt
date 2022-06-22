package com.chaitrika.expensemanagement.transactionManagementModule.presentation.transactionManagement.allTransactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.chaitrika.expensemanagement.transactionManagementModule.domain.TransactionApiRepository
import com.chaitrika.expensemanagement.utils.dataUtils.Resource
import com.chaitrika.expensemanagement.utils.widgetUtils.BaseFragment
import com.chaitrika.expensemanagement.R
import com.chaitrika.expensemanagement.databinding.FragmentAllTransactionsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AllTransactionsFragment : BaseFragment<FragmentAllTransactionsBinding>() {

    private val allTransactionAdapter by lazy {
        AllTransactionsAdapter()
    }

    @Inject
    lateinit var transactionApiRepository: TransactionApiRepository

    private val allTransactionViewModel: AllTransactionViewModel by viewModels()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAllTransactionsBinding {
        return FragmentAllTransactionsBinding.inflate(layoutInflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            allTransactionsRv.layoutManager = LinearLayoutManager(applicationContext())
            allTransactionsRv.adapter = allTransactionAdapter

            allTransactionsSearch.doOnTextChanged { text, _, _, _ ->
                allTransactionViewModel.getAllTransactionsBySearch(text.toString())
            }
        }

        allTransactionAdapter.setOnTransactionClickListener {
            val bundle = bundleOf("id" to it.id)
            findNavController().navigate(
                R.id.action_allTransactionFragment_to_transactionFragment,
                bundle
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                allTransactionViewModel.getAllTransaction()

                allTransactionViewModel.transactionData.collect { response ->
                    if (response is Resource.Empty) {
                        delay(100)
                    } else if (response is Resource.Success) {
                        allTransactionAdapter.transactions = response.data!!
                    }
                }
            }
        }

    }
}