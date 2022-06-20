package com.mhirrr.expensemanagement.utils.widgetUtils

import android.app.DatePickerDialog
import android.content.Context
import android.view.View
import android.widget.DatePicker
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class DatePickerExtender(context: Context, textInputEditText: TextInputEditText) : View.OnClickListener,
    DatePickerDialog.OnDateSetListener {

    private var _textInput: TextInputEditText = textInputEditText
    private var _context: Context = context
    private var _month: Int = 0
    private var _year: Int = 0
    private var _dayOfMonth: Int = 0

    private var _calendar = Calendar.getInstance()


    init {
        _textInput.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        DatePickerDialog(
            _context,
            this,
            _calendar.get(Calendar.YEAR),
            _calendar.get(Calendar.MONTH),
            _calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        this._month = month
        this._year = year
        this._dayOfMonth = dayOfMonth

        updateDisplay()
    }

    private fun updateDisplay() {
        _calendar.set(Calendar.YEAR, _year)
        _calendar.set(Calendar.MONTH, _month)
        _calendar.set(Calendar.DAY_OF_MONTH, _dayOfMonth)

        val format = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(format, Locale.US)

        _textInput.setText(sdf.format(_calendar.time))
    }
}