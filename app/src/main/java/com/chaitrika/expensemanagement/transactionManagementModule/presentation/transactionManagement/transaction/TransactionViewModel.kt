package com.chaitrika.expensemanagement.transactionManagementModule.presentation.transactionManagement.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chaitrika.expensemanagement.transactionManagementModule.data.local.TransactionModel
import com.chaitrika.expensemanagement.transactionManagementModule.domain.TransactionApiRepository
import com.chaitrika.expensemanagement.utils.dataUtils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val transactionApiRepository: TransactionApiRepository
) : ViewModel() {

    private val _transaction = MutableStateFlow<Resource<TransactionModel>>(Resource.Empty())
    val transaction: StateFlow<Resource<TransactionModel>> = _transaction

    fun getTransaction(id: Int) {
        viewModelScope.launch {
            val result = transactionApiRepository.getTransactionById(id.toLong())
            _transaction.value = Resource.Success(result!!)
        }
    }

    fun insertTransaction(transaction: TransactionModel) {
        viewModelScope.launch {
            transactionApiRepository.insertTransaction(transaction)
        }
    }

}