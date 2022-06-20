package com.mhirrr.expensemanagement.transactionManagementModule.presentation.transactionManagement.allTransactions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mhirrr.expensemanagement.transactionManagementModule.data.local.TransactionModel
import com.mhirrr.expensemanagement.R
import com.mhirrr.expensemanagement.databinding.RvTransactionCardBinding

class AllTransactionsAdapter :
    RecyclerView.Adapter<AllTransactionsAdapter.TransactionViewHolder>() {

    var transactions: List<TransactionModel>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    inner class TransactionViewHolder(val binding: RvTransactionCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        return TransactionViewHolder(
            RvTransactionCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]

        with(holder.binding) {
            rvTransactionName.text = transaction.title
            rvTransactionDescription.text = transaction.description

            rvTransactionAmount.text = if (transaction.type == "income") {
                "+₹${transaction.amount}"
            } else {
                "-₹${transaction.amount}"
            }

            if (transaction.type == "income") {
                rvTransactionAmount.setTextColor(
                    ContextCompat.getColor(
                        rvTransactionAmount.context,
                        R.color.income
                    )
                )
            } else {
                rvTransactionAmount.setTextColor(
                    ContextCompat.getColor(
                        rvTransactionAmount.context,
                        R.color.expense
                    )
                )
            }

            when (transaction.category) {
                "housing" -> {
                    rvTransactionLogo.setImageResource(R.drawable.ic_food)
                }
                "transportation" -> {
                    rvTransactionLogo.setImageResource(R.drawable.ic_transport)
                }
                "food" -> {
                    rvTransactionLogo.setImageResource(R.drawable.ic_food)
                }
                "utilities" -> {
                    rvTransactionLogo.setImageResource(R.drawable.ic_utilities)
                }
                "insurance" -> {
                    rvTransactionLogo.setImageResource(R.drawable.ic_insurance)
                }
                "healthcare" -> {
                    rvTransactionLogo.setImageResource(R.drawable.ic_medical)
                }
                "saving & debts" -> {
                    rvTransactionLogo.setImageResource(R.drawable.ic_savings)
                }
                "personal spending" -> {
                    rvTransactionLogo.setImageResource(R.drawable.ic_personal_spending)
                }
                "entertainment" -> {
                    rvTransactionLogo.setImageResource(R.drawable.ic_entertainment)
                }
                "miscellaneous" -> {
                    rvTransactionLogo.setImageResource(R.drawable.ic_others)
                }
                else -> {
                    rvTransactionLogo.setImageResource(R.drawable.ic_others)
                }
            }
        }

        holder.itemView.setOnClickListener {
            onTransactionClickListener?.let { it(transaction) }
        }
    }

    private var onTransactionClickListener: ((TransactionModel) -> Unit)? = null
    fun setOnTransactionClickListener(listener: ((TransactionModel) -> Unit)?) {
        this.onTransactionClickListener = listener
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    private val diffUtil = object : DiffUtil.ItemCallback<TransactionModel>() {
        override fun areItemsTheSame(
            oldItem: TransactionModel,
            newItem: TransactionModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: TransactionModel,
            newItem: TransactionModel
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffUtil)

}