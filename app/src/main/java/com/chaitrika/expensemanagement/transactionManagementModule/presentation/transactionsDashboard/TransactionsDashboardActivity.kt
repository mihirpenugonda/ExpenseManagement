package com.chaitrika.expensemanagement.transactionManagementModule.presentation.transactionsDashboard

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.developer.finance_refactored.transactionManagementModule.presentation.transactionsDashboard.TransactionDashboardViewModel
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.tabs.TabLayout
import com.chaitrika.expensemanagement.databinding.ActivityTransactionsDashboardBinding
import com.chaitrika.expensemanagement.transactionManagementModule.data.local.TransactionModel
import com.chaitrika.expensemanagement.transactionManagementModule.presentation.addTransaction.AddTransactionActivity
import com.chaitrika.expensemanagement.transactionManagementModule.presentation.transactionManagement.TransactionManagementActivity
import com.chaitrika.expensemanagement.utils.dataUtils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class TransactionsDashboardActivity : AppCompatActivity() {

    private val transactionDashboardViewModel: TransactionDashboardViewModel by viewModels()

    private lateinit var binding: ActivityTransactionsDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionsDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            transactionsAddButton.setOnClickListener {
                startActivity(Intent(applicationContext, AddTransactionActivity::class.java))
            }

            transactionsTotalBalanceCard.setOnClickListener {
                startActivity(
                    Intent(
                        applicationContext,
                        TransactionManagementActivity::class.java
                    )
                )
            }

            transactionFilterTab.addOnTabSelectedListener(object :
                TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    setFilter(tab?.text.toString())
                    when (tab?.text) {
                        "overall" -> {
                            binding.transactionsTotalBalanceCard.visibility = View.VISIBLE
                            binding.totalExpenseCardView.visibility = View.VISIBLE
                            binding.totalIncomeCardView.visibility = View.VISIBLE
                            binding.transactionsLayoutSpacer.visibility = View.VISIBLE
                        }
                        "income" -> {
                            binding.transactionsTotalBalanceCard.visibility = View.GONE
                            binding.totalExpenseCardView.visibility = View.GONE
                            binding.totalIncomeCardView.visibility = View.VISIBLE
                            binding.transactionsLayoutSpacer.visibility = View.GONE
                        }
                        "expense" -> {
                            binding.transactionsTotalBalanceCard.visibility = View.GONE
                            binding.totalExpenseCardView.visibility = View.VISIBLE
                            binding.totalIncomeCardView.visibility = View.GONE
                            binding.transactionsLayoutSpacer.visibility = View.GONE
                        }
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })
        }

        lifecycleScope.launchWhenCreated {
            transactionDashboardViewModel.getAllTransactions()

            transactionDashboardViewModel.dashboardContent.collectLatest {
                if (it is Resource.Success) {
                    populateData(it.data!!)
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            transactionDashboardViewModel.getAllTransactions()
        }
    }

    private fun setFilter(filter: String) {
        lifecycleScope.launch {
            if (filter == "overall") {
                transactionDashboardViewModel.getAllTransactions()
            } else {
                transactionDashboardViewModel.getAllTransactionsByFilter(filter)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun populateData(data: List<TransactionModel>) {
        val (incomeList, expenseList) = data.partition { it.type == "income" }
        val totalIncome = incomeList.sumOf { Integer.parseInt(it.amount) }
        val totalExpense = expenseList.sumOf { Integer.parseInt(it.amount) }


        if (binding.transactionFilterTab.selectedTabPosition == 0) overviewPieChart(
            totalIncome,
            totalExpense
        )
        else filteredPieChart(data)

        var balance = totalIncome - totalExpense

        binding.transactionsTotalBalanceAmount.text = if (balance < 0) {
            balance *= -1; "-₹$balance"
        } else "+₹$balance"

        binding.transactionsTotalExpenseAmount.text = "-₹${totalExpense}"
        binding.transactionsTotalIncomeAmount.text = "+₹${totalIncome}"

        binding.transactionsPieChart.invalidate()
    }

    private fun filteredPieChart(data: List<TransactionModel>) {
        val totalsHashMap = hashMapOf(
            "housing" to 0,
            "transportation" to 0,
            "food" to 0,
            "utilities" to 0,
            "insurance" to 0,
            "healthcare" to 0,
            "saving & debts" to 0,
            "personal spending" to 0,
            "entertainment" to 0,
            "miscellaneous" to 0
        )

        val colorsString = listOf(
            "#003f5c",
            "#2f4b7c",
            "#665191",
            "#a05195",
            "#d45087",
            "#f95d6a",
            "#ff7c43",
            "#ffa600",
            "#aa8600",
            "#aa3f5c"
        )

        val colors = ArrayList<Int>()
        for (color in colorsString) {
            colors.add(Color.parseColor(color))
        }

        for (item in data) {
            totalsHashMap[item.category] = totalsHashMap[item.category]!! + item.amount.toInt()
        }

        val pieEntries = ArrayList<PieEntry>()
        for (total in totalsHashMap) {
            if (total.value == 0) continue
            pieEntries.add(PieEntry(total.value.toFloat(), total.key))
        }

        val pieDataSet = PieDataSet(pieEntries, "")
        pieDataSet.valueTextSize = 16f
        pieDataSet.colors = colors
        val pieData = PieData(pieDataSet)
        pieData.setDrawValues(true)

        with(binding.transactionsPieChart) {
            this.data = pieData
            setEntryLabelTextSize(10f)

            isDrawHoleEnabled = false
            legend.isWordWrapEnabled = true
            legend.setDrawInside(false)
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        }

    }

    private fun overviewPieChart(totalIncome: Int, totalExpense: Int) {
        val typeAmountMap = HashMap<String, Int>()
        typeAmountMap["income"] = totalIncome
        typeAmountMap["expense"] = totalExpense

        val colors = ArrayList<Int>()
        colors.add(Color.parseColor("#FA6FCF97"))
        colors.add(Color.parseColor("#FAEB5757"))

        val pieEntries = ArrayList<PieEntry>()
        pieEntries.add(PieEntry(typeAmountMap["income"]!!.toFloat(), "income"))
        pieEntries.add(PieEntry(typeAmountMap["expense"]!!.toFloat(), "expense"))

        val pieDataSet = PieDataSet(pieEntries, "")
        pieDataSet.valueTextSize = 16f
        pieDataSet.colors = colors
        val pieData = PieData(pieDataSet)
        pieData.setDrawValues(true)

        with(binding.transactionsPieChart) {
            this.data = pieData
            setEntryLabelTextSize(10f)
            legend.textColor = Color.WHITE
            isDrawHoleEnabled = false
        }

    }

    override fun onStart() {
        lifecycleScope.launch { transactionDashboardViewModel.getAllTransactions() }
        super.onStart()
    }

}