package com.mhirrr.expensemanagement.transactionManagementModule.presentation.transactionManagement.allTransactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mhirrr.expensemanagement.transactionManagementModule.data.local.TransactionModel
import com.mhirrr.expensemanagement.transactionManagementModule.domain.TransactionApiRepository
import com.mhirrr.expensemanagement.utils.dataUtils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllTransactionViewModel @Inject constructor(
    private val transactionApiRepository: TransactionApiRepository
) : ViewModel() {

    private val _transactionData =
        MutableStateFlow<Resource<List<TransactionModel>>>(Resource.Empty())
    val transactionData: StateFlow<Resource<List<TransactionModel>>> = _transactionData


    private var searchJob: Job? = null

    fun getAllTransaction() {
        viewModelScope.launch {
            transactionApiRepository.getAllTransactions().collect {
                _transactionData.value = Resource.Success(it)
            }
        }
    }

    fun getAllTransactionsBySearch(search: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)
            val data = transactionApiRepository.getTransactionBySearch(search)
            _transactionData.value = Resource.Success(data)
        }
    }

}