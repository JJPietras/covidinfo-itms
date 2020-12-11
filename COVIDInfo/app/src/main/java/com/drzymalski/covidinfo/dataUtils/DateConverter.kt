package com.drzymalski.covidinfo.dataUtils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

class DateConverter {
    companion object{
        fun formatDateFull(date: Date, today: Boolean=false): String{
            val month = if (date.month + 1< 10) "0" +(date.month + 1).toString() else (date.month + 1).toString()
            val day = if (date.date < 10) "0" + date.date.toString() else date.date.toString()
            var dateNew = "${(date.year + 1900)}-$month-$day"
            if (dateNew < "2019-01-01") dateNew = getTodayDate()
            return dateNew
        }

        fun formatDateShort(date: Date): String{
            val month = if (date.month + 1< 10) "0" +(date.month + 1).toString() else (date.month + 1).toString()
            val day = if (date.date < 10) "0" + date.date.toString() else date.date.toString()
            return "$month-$day"
        }

        @SuppressLint("SimpleDateFormat")
        fun getTodayDate(): String{
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            return sdf.format(Date())
        }

    }
}