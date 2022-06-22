package com.chaitrika.expensemanagement.utils.dataUtils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.util.*

class DateTimeConverters {

    @RequiresApi(Build.VERSION_CODES.O)
    fun stringDateToLong(
        stringDate: String
    ): Long {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        val date = sdf.parse(stringDate)

        return date!!.toInstant()!!.toEpochMilli()
    }

    fun longToStringDate(
        longDate: Long
    ): String {
        val format = "dd/MM/yy"
        val sdf = SimpleDateFormat(format, Locale.US)

        return sdf.format(longDate)
    }

}