package com.developer.finance_refactored.transactionManagementModule.presentation.transactionsDashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mhirrr.expensemanagement.transactionManagementModule.data.local.TransactionModel
import com.mhirrr.expensemanagement.transactionManagementModule.domain.TransactionApiRepository
import com.mhirrr.expensemanagement.utils.dataUtils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionDashboardViewModel @Inject constructor(
    private val transactionApiRepository: TransactionApiRepository
) : ViewModel() {

    private val _dashboardContent =
        MutableStateFlow<Resource<List<TransactionModel>>>(Resource.Empty())
    val dashboardContent: StateFlow<Resource<List<TransactionModel>>> = _dashboardContent

    fun getAllTransactions() {
        viewModelScope.launch {
            val transactions = transactionApiRepository.getAllTransactions().first()
            _dashboardContent.value = Resource.Success(transactions)
        }
    }

    fun getAllTransactionsByFilter(typeFilter: String) {
        viewModelScope.launch {
            transactionApiRepository.getAllTransactions().map { response ->
                response.filter { it.type == typeFilter }
            }.collect { response ->
                _dashboardContent.value =
                    Resource.Success(response)
            }
        }
    }

}