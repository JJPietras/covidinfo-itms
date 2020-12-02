package com.drzymalski.covidinfo.dataUtils

import java.util.*

class DateConverter {
    companion object{
        fun formatDateFull(date: Date): String{
            val month = if (date.month + 1< 10) "0" +(date.month + 1).toString() else (date.month + 1).toString()
            val day = if (date.date < 10) "0" + date.date.toString() else date.date.toString()
            return "${(date.year + 1900)}-$month-$day"
        }

        fun formatDateShort(date: Date): String{
            val month = if (date.month + 1< 10) "0" +(date.month + 1).toString() else (date.month + 1).toString()
            val day = if (date.date < 10) "0" + date.date.toString() else date.date.toString()
            return "$month-$day"
        }

    }
}