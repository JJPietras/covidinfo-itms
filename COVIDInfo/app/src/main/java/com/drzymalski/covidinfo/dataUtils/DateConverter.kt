package com.drzymalski.covidinfo.dataUtils

import android.annotation.SuppressLint
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ln


class DateConverter {
    companion object{
        fun formatDateFull(date: Date): String{
            val month = if (date.month + 1< 10) "0" +(date.month + 1).toString() else (date.month + 1).toString()
            val day = if (date.date < 10) "0" + date.date.toString() else date.date.toString()
            var dateNew = "${(date.year + 1900)}-$month-$day"
            if (dateNew < "2019-01-01") dateNew = getTodayDate()
            return dateNew
        }

        fun formatDateShort(date: Date?): String{
            if (date==null) return ""
            val month = if (date.month + 1< 10) "0" +(date.month + 1).toString() else (date.month + 1).toString()
            val day = if (date.date < 10) "0" + date.date.toString() else date.date.toString()
            return "$month-$day"
        }

        @SuppressLint("SimpleDateFormat")
        fun getTodayDate(): String{
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            return sdf.format(Date())
        }

        @SuppressLint("SimpleDateFormat")
        fun getTodayDateShort(): String{
            val sdf = SimpleDateFormat("MM-dd")
            return sdf.format(Date())
        }

        @SuppressLint("SimpleDateFormat")
        fun getDateFromString(string: String): Date? {
            return SimpleDateFormat("dd-MM-yyyy").parse(string)
        }

        @SuppressLint("SimpleDateFormat")
        fun getAddDaysToDate(lastDate: String, daysToAdd: Int, shortFormat: Boolean = false): String{
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val ssdf = SimpleDateFormat("MM-dd")
            val c = Calendar.getInstance()
            c.time = sdf.parse(lastDate)
            c.add(Calendar.DATE, daysToAdd) // number of days to add
            if (shortFormat) return ssdf.format(c.time)
            return sdf.format(c.time)
        }

        fun coolNumberFormat(count: Float): String {
            if (count < 1000) return "" + count
            val exp = (ln(count.toDouble()) / ln(1000.0)).toInt()
            val format = DecimalFormat("0.#")
            val value: String = format.format(count / Math.pow(1000.0, exp.toDouble()))
            return String.format("%s%c", value, "kMBTPE"[exp - 1])
        }
    }
}